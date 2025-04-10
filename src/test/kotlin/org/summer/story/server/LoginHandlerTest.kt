package org.summer.story.server

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.SocketChannel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.summer.story.server.dtos.OutDto
import org.summer.story.server.game.LoginGameProcessorFactory
import java.net.InetSocketAddress

class LoginHandlerTest {
    private lateinit var handler: LoginHandler
    private val globalState: GlobalState = GlobalState()
    private val ctx: ChannelHandlerContext = mockk(relaxed = true)
    private val channel: Channel = mockk(relaxed = true)
    private val timeService: TimeService = mockk(relaxed = true)
    private val sendPacketService: SendPacketService = mockk(relaxed = true)
    private val socketChannel: SocketChannel = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        every {socketChannel.remoteAddress()} returns InetSocketAddress("127.0.0.1", 1234)

        handler = LoginHandler(
            globalState,
            timeService,
            sendPacketService,
            mockk(relaxed = true),
            LoginGameProcessorFactory(emptyList()),
            NetworkContext(socketChannel)
        )
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

    @Test
    fun `should send ping packet when idle event is triggered`() {
        // Suppose
        every { channel.isActive } returns true
        every { ctx.channel() } returns channel
        every { timeService.currentTimeMillis() } returns 1000L
        every { sendPacketService.sendPacket(any(), any()) } returns mockk(relaxed = true)

        // Given
        globalState.serverState = MapleServerState.RUNNING
        handler.channelActive(ctx)

        // When
        handler.userEventTriggered(ctx, Any())

        // Then
        verify { sendPacketService.sendPacket(channel, match<OutDto> { it.toPacket().getBytes()[0] == SendOpcode.PING.value.toByte() }) }
    }

    @Test
    fun `should not send ping packet when channel is inactive`() {
        // Suppose
        every { channel.isActive } returns false
        every { ctx.channel() } returns channel

        // Given
        globalState.serverState = MapleServerState.RUNNING
        handler.channelActive(ctx)

        // When
        handler.userEventTriggered(ctx, Any())

        // Then
        verify(exactly = 0) { sendPacketService.sendPacket(any(), any()) }
    }
}