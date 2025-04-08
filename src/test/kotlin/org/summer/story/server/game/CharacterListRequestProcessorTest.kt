package org.summer.story.server.game

import io.mockk.every
import io.mockk.mockk
import io.netty.channel.socket.SocketChannel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.dsl.module
import org.summer.story.DatabaseEnabledTest
import org.summer.story.InPacketFactory
import org.summer.story.MockPlayer
import org.summer.story.server.NetworkContext
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.dtos.CharacterListOutDto
import org.summer.story.server.dtos.ServerStatusOutDto
import org.summer.story.server.players.PlayerImpl
import org.summer.story.server.worlds.GameChannelImpl
import org.summer.story.server.worlds.World
import org.summer.story.server.worlds.WorldManager
import java.net.Inet4Address
import java.net.InetSocketAddress
import kotlin.test.assertEquals

class CharacterListRequestProcessorTest : DatabaseEnabledTest() {
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
        player = createMockPlayer()
    }

    @Test
    fun `test getOpcode returns CHARACTER_LIST_REQUEST`() {
        // Assert
        val processor = koin.get<CharacterListRequestProcessor>()
        assertEquals(ReceiveOpcode.CHARACTER_LIST_REQUEST, processor.getOpcode())
    }

    @Test
    fun `test process for valid world and channel request`() {
        // When
        val processor = koin.get<CharacterListRequestProcessor>()
        processor.process(player, InPacketFactory.createCharacterListRequestWithoutOpcode(0, 0))

        // Then
        player.responds.single().let {
            val dto = it as CharacterListOutDto
            assertEquals(dto.availableCharacterSlot, 3)
        }
    }

    @Test
    fun `test process for invalid world ID`() {
        // When
        val invalidWorldId = 1
        val processor = koin.get<CharacterListRequestProcessor>()
        processor.process(player, InPacketFactory.createCharacterListRequestWithoutOpcode(invalidWorldId, 0))

        // Then
        player.responds.single().let {
            val dto = it as ServerStatusOutDto
            assertEquals(dto.worldServerStatus, WorldServerStatus.NOT_AVAILABLE)
        }
    }

    @Test
    fun `test process for negative channel ID`() {
        // When
        val negativeChannelId = -1
        val processor = koin.get<CharacterListRequestProcessor>()
        processor.process(player, InPacketFactory.createCharacterListRequestWithoutOpcode(0, negativeChannelId))

        // Then
        player.responds.single().let {
            val dto = it as ServerStatusOutDto
            assertEquals(dto.worldServerStatus, WorldServerStatus.NOT_AVAILABLE)
        }
    }

    @Test
    fun `test process for channel ID out of range`() {
        // When
        val outOfRangeChannelId = 2
        val processor = koin.get<CharacterListRequestProcessor>()
        processor.process(player, InPacketFactory.createCharacterListRequestWithoutOpcode(0, outOfRangeChannelId))

        // Then
        player.responds.single().let {
            val dto = it as ServerStatusOutDto
            assertEquals(dto.worldServerStatus, WorldServerStatus.NOT_AVAILABLE)
        }
    }

    @Test
    fun `test process when world is full`() {
        // Given
        val world = mockk<World>(relaxed = true)
        val whatEver = 10
        every { world.channels } returns listOf(GameChannelImpl(world, 0, whatEver))
        every { world.maxPlayers } returns 10
        every { world.onlinePlayers } returns 10

        val worldManager = mockk<WorldManager>(relaxed = true)
        every { worldManager.world } returns world

        koin.loadModules(listOf(
            module {
                single { worldManager }
            }
        ))

        // When
        val processor = koin.get<CharacterListRequestProcessor>()
        processor.process(player, InPacketFactory.createCharacterListRequestWithoutOpcode(0, 0))

        // Then
        player.responds.single().let {
            val dto = it as ServerStatusOutDto
            assertEquals(dto.worldServerStatus, WorldServerStatus.NOT_AVAILABLE)
        }
    }
}