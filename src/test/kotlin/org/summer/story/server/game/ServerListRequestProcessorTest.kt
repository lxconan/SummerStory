package org.summer.story.server.game

import io.mockk.every
import io.mockk.mockk
import io.netty.channel.socket.SocketChannel
import org.junit.jupiter.api.Test
import org.summer.story.DatabaseEnabledTest
import org.summer.story.InPacketFactory
import org.summer.story.MockPlayer
import org.summer.story.server.NetworkContext
import org.summer.story.server.dtos.WorldInformationOutDto
import org.summer.story.server.dtos.WorldInformationCompleteOutDto
import org.summer.story.server.dtos.RecommendedWorldOutDto
import org.summer.story.server.dtos.LastConnectedWorldOutDto
import org.summer.story.server.players.PlayerImpl
import org.summer.story.server.worlds.WorldDefinitions
import java.net.Inet4Address
import java.net.InetSocketAddress
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServerListRequestProcessorTest : DatabaseEnabledTest() {
    private fun createMockPlayer(): MockPlayer {
        val channel = mockk<SocketChannel>()
        every { channel.isActive } returns true
        every { channel.remoteAddress() } returns InetSocketAddress(Inet4Address.getLoopbackAddress(), 12345)
        val internal = PlayerImpl(koin.get(), koin.get(), NetworkContext(channel))
        return MockPlayer(internal)
    }

    @Test
    fun `should send world information`() {
        // Given
        val player = createMockPlayer()

        // When
        val processor = koin.get<ServerListRequestProcessor>()
        processor.process(player, InPacketFactory.createServerListRequestWithoutOpcode())

        // Then
        player.responds[0].let {
            val dto = it as WorldInformationOutDto
            assertEquals(WorldDefinitions.worldName, dto.world.name)
            assertEquals(1, dto.world.id)
            assertEquals(0, dto.world.flag)
            assertEquals("Welcome to SummerStory!", dto.world.eventMessage)
            assertEquals(1, dto.world.channels.size)

            (1..dto.world.channels.size).forEach { index ->
                val channel = dto.world.channels[index - 1]
                assertEquals(index, channel.id)
                assertEquals(WorldDefinitions.maxPlayerPerChannel, channel.playerCapacity)
            }
        }

        assertTrue(player.responds[1] is WorldInformationCompleteOutDto)
        assertTrue(player.responds[2] is LastConnectedWorldOutDto)
        assertTrue(player.responds[3] is RecommendedWorldOutDto)
    }
}