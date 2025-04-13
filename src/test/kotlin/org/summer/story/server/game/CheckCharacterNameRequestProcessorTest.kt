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
import org.summer.story.server.login.dtos.CharacterNameResponseOutDto
import org.summer.story.server.login.game.CheckCharacterNameRequestProcessor
import org.summer.story.server.players.PlayerImpl
import java.net.Inet4Address
import java.net.InetSocketAddress
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckCharacterNameRequestProcessorTest : DatabaseEnabledTest() {
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
    fun `test process with valid character name`() {
        // Given
        val validName = "validName"
        val processor = koin.get<CheckCharacterNameRequestProcessor>()

        // When
        processor.process(
            player,
            InPacketFactory.createCharacterNameRequestWithoutOpCode(validName, configuration.packet.charsetObject))

        // Then
        player.responds.single().let {
            val respond = it as CharacterNameResponseOutDto
            assertEquals(validName, respond.name)
            assertFalse(respond.forbidden)
        }
    }

    @Test
    fun `test process with invalid character name`() {
        // Given
        val invalidName = "shitName"
        val processor = koin.get<CheckCharacterNameRequestProcessor>()

        // When
        processor.process(
            player,
            InPacketFactory.createCharacterNameRequestWithoutOpCode(invalidName, configuration.packet.charsetObject))

        // Then
        player.responds.single().let {
            val respond = it as CharacterNameResponseOutDto
            assertEquals(invalidName, respond.name)
            assertTrue(respond.forbidden)
        }
    }
}