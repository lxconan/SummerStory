package org.summer.story.server.game

import io.mockk.every
import io.mockk.mockk
import io.netty.channel.socket.SocketChannel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.summer.story.DatabaseEnabledTest
import org.summer.story.InPacketFactory
import org.summer.story.MockPlayer
import org.summer.story.server.NetworkContext
import org.summer.story.server.dtos.ServerStatusOutDto
import org.summer.story.server.players.PlayerImpl
import org.summer.story.server.worlds.GameChannelImpl
import org.summer.story.server.worlds.WorldManager
import java.net.Inet4Address
import java.net.InetSocketAddress
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServerStatusRequestProcessorTest : DatabaseEnabledTest() {
    private lateinit var worldManager: WorldManager
    private lateinit var player: MockPlayer

    private fun createMockPlayer(): MockPlayer {
        val channel = mockk<SocketChannel>()
        every { channel.isActive } returns true
        every { channel.remoteAddress() } returns InetSocketAddress(Inet4Address.getLoopbackAddress(), 12345)
        val internal = PlayerImpl(koin.get(), koin.get(), NetworkContext(channel))
        return MockPlayer(internal)
    }

    @BeforeEach
    fun setUp() {
        worldManager = WorldManager()
        player = createMockPlayer()
    }

    @Test
    fun `test process with not existed world`() {
        // Given
        val processor = ServerStatusRequestProcessor(worldManager)
        val notExistedIndex = 1

        // When
        processor.process(player, InPacketFactory.createServerStatusRequestWithoutOpcode(notExistedIndex))

        // Then
        player.responds.single().let {
            assertTrue(it is ServerStatusOutDto)
            assertEquals(WorldServerStatus.NOT_AVAILABLE, it.worldServerStatus)
        }
    }

    @Test
    fun `test process with existed world and available capacity zero`() {
        // Given
        val processor = ServerStatusRequestProcessor(worldManager)
        val world = worldManager.world
        for (channel in world.channels) {
            repeat(channel.maxPlayers) { (channel as GameChannelImpl).increasePlayerOrFail() }
        }

        // When
        processor.process(player, InPacketFactory.createServerStatusRequestWithoutOpcode(0))

        // Then
        player.responds.single().let {
            assertTrue(it is ServerStatusOutDto)
            assertEquals(WorldServerStatus.NOT_AVAILABLE, it.worldServerStatus)
        }
    }

    @Test
    fun `test process with existed world and available capacity less than 20 percent`() {
        // Given
        val processor = ServerStatusRequestProcessor(worldManager)
        val world = worldManager.world
        for (channel in world.channels) {
            repeat(channel.maxPlayers - 2) { (channel as GameChannelImpl).increasePlayerOrFail() }
        }

        // When
        processor.process(player, InPacketFactory.createServerStatusRequestWithoutOpcode(0))

        // Then
        player.responds.single().let {
            assertTrue(it is ServerStatusOutDto)
            assertEquals(WorldServerStatus.FULL, it.worldServerStatus)
        }
    }

    @Test
    fun `test process with existed world and available capacity more than 20 percent`() {
        // Given
        val processor = ServerStatusRequestProcessor(worldManager)
        val world = worldManager.world
        for (channel in world.channels) {
            repeat(2) { (channel as GameChannelImpl).increasePlayerOrFail() }
        }

        // When
        processor.process(player, InPacketFactory.createServerStatusRequestWithoutOpcode(0))

        // Then
        player.responds.single().let {
            assertTrue(it is ServerStatusOutDto)
            assertEquals(WorldServerStatus.NORMAL, it.worldServerStatus)
        }
    }
}