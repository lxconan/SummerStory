package org.summer.story.server

import io.mockk.*
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginHandlerTest {
    private lateinit var handler: LoginHandler
    private lateinit var globalState: GlobalState
    private lateinit var ctx: ChannelHandlerContext
    private lateinit var channel: Channel

    @BeforeEach
    fun setup() {
        globalState = GlobalState()
        handler = LoginHandler(globalState)
    }

    @Test
    fun `should allow connection when server is running`() {
        // Suppose
        ctx = mockk<ChannelHandlerContext>()
        channel = mockk<Channel>()
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
        ctx = mockk<ChannelHandlerContext>()
        channel = mockk<Channel>(relaxed = true)
        every { ctx.channel() } returns channel

        // Given
        globalState.serverState = MapleServerState.SHUTDOWN

        // When
        handler.channelActive(ctx)

        // Then
        verify { channel.close() }
    }
}