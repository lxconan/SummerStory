package org.summer.story.server

import io.mockk.*
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginHandlerTest {
    private lateinit var handler: LoginHandler
    private val globalState: GlobalState = GlobalState()
    private val ctx: ChannelHandlerContext = mockk(relaxed = true)
    private val channel: Channel = mockk(relaxed = true)
    private val timeService: TimeService = mockk(relaxed = true)
    private val sendPacketService: SendPacketService = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        handler = LoginHandler(globalState, timeService, sendPacketService)
    }

    @Test
    fun `should allow connection when server is running`() {
        // Suppose
        every { channel.close() } returns null
        every { ctx.channel() } returns channel

        // Given
        globalState.serverState = MapleServerState.RUNNING

        // When
        handler.channelActive(ctx)

        // Then
        verify(inverse = true) { channel.close() }
    }

    @Test
    fun `should close connection when server is not running`() {
        // Suppose
        every { ctx.channel() } returns channel

        // Given
        globalState.serverState = MapleServerState.SHUTDOWN

        // When
        handler.channelActive(ctx)

        // Then
        verify { channel.close() }
    }
}