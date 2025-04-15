package org.summer.story.server.login.game

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.summer.story.net.packet.InPacket
import org.summer.story.server.login.LoginReceiveOpcode
import org.summer.story.server.players.Player
import kotlin.test.*

class KeepAliveProcessorTest {

    private lateinit var processor: KeepAliveProcessor
    private lateinit var player: Player
    private lateinit var inPacket: InPacket

    @BeforeEach
    fun setup() {
        processor = KeepAliveProcessor()
        player = mockk(relaxed = true)
        inPacket = mockk()
    }

    @Test
    fun `should return correct opcode`() {
        // When
        val opcode = processor.getOpcode()

        // Then
        assertEquals(LoginReceiveOpcode.PONG, opcode, "Opcode should be PONG")
    }

    @Test
    fun `should update player's last pong time`() {
        // Given
        every { player.pongReceived() } returns Unit

        // When
        processor.process(player, inPacket)

        // Then
        verify { player.pongReceived() }
    }
}