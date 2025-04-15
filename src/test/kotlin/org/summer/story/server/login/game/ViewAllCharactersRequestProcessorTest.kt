package org.summer.story.server.login.game

import io.mockk.every
import io.mockk.mockk
import io.netty.channel.socket.SocketChannel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.summer.story.DatabaseEnabledTest
import org.summer.story.MockPlayer
import org.summer.story.net.packet.InPacket
import org.summer.story.server.NetworkContext
import org.summer.story.server.login.LoginReceiveOpcode
import org.summer.story.server.login.dtos.CannotFindAnyCharactersOutDto
import org.summer.story.server.players.PlayerImpl

class ViewAllCharactersRequestProcessorTest : DatabaseEnabledTest() {
    private lateinit var processor: ViewAllCharactersRequestProcessor
    private lateinit var player: MockPlayer
    private lateinit var packet: InPacket

    @BeforeEach
    fun setUp() {
        val channel = mockk<SocketChannel>(relaxed = true)
        every { channel.isActive } returns true
        player = MockPlayer(PlayerImpl(koin.get(), koin.get(), NetworkContext(channel)))
        packet = mockk(relaxed = true)

        processor = koin.get<ViewAllCharactersRequestProcessor>()
    }

    @Test
    fun `should have the correct opcode`() {
        assertEquals(LoginReceiveOpcode.VIEW_ALL_CHARACTERS_REQUEST, processor.getOpcode())
    }

    @Test
    fun `should send cannot find characters message`() {
        // When
        processor.process(player, packet)

        // Then
        player.responds.single().let {
            assertTrue(it is CannotFindAnyCharactersOutDto)
        }
    }
}