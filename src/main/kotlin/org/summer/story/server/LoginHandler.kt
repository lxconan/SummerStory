package org.summer.story.server

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.LoggerFactory

class LoginHandler(
    private val serverState: GlobalState,
    private val timeService: TimeService,
    private val sendPacketService: SendPacketService
) : ChannelInboundHandlerAdapter() {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginHandler::class.java)
    }

    private lateinit var ioChannel: Channel

    override fun channelActive(ctx: ChannelHandlerContext) {
        val currentServerState = serverState.serverState
        val channel: Channel = ctx.channel()
        if (currentServerState != MapleServerState.RUNNING) {
            logger.error("Client connected to login server while server is: {}. The channel will be closed", currentServerState)
            channel.close()
            return
        }

        ioChannel = channel
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        val pingedAt: Long = timeService.currentTimeMillis()
    }
}
