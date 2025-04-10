package org.summer.story.server.game

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.summer.story.server.ReceiveOpcode
import kotlin.test.*

class LoginServerLoginGameProcessorFactoryTest {

    @Test
    fun `should return processor for registered opcode`() {
        // Given
        val processor = mockk<LoginServerGameProcessor>()
        every { processor.getOpcode() } returns ReceiveOpcode.PONG
        val factory = LoginGameProcessorFactory(listOf(processor))

        // When
        val result = factory.getGameProcessor(ReceiveOpcode.PONG.value.toShort())

        // Then
        assertNotNull(result, "Processor should not be null for registered opcode")
        assertSame(result, processor)
    }

    @Test
    fun `should return null for unregistered opcode`() {
        // Given
        val factory = LoginGameProcessorFactory(emptyList())

        // When
        val result = factory.getGameProcessor(ReceiveOpcode.PONG.value.toShort())

        // Then
        assertNull(result, "Processor should be null for unregistered opcode")
    }
}