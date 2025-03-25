package org.summer.story.server

import dev.starry.ktscheduler.scheduler.KtScheduler
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.LoggerFactory
import org.summer.story.server.dtos.PingOutDto
import java.time.ZonedDateTime

class LoginHandler(
    private val serverState: GlobalState,
    private val timeService: TimeService,
    private val sendPacketService: SendPacketService,
    private val scheduler: KtScheduler
) : ChannelInboundHandlerAdapter() {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginHandler::class.java)
    }

    private lateinit var ioChannel: Channel
    private val loginHandlerContext = LoginHandlerContext()

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
        if (!ioChannel.isActive) { return }
        pingClientToCheckIdle()
    }

    private fun pingClientToCheckIdle() {
        val pingedAt: Long = timeService.currentTimeMillis()
        val pingPongMaxDelaySeconds: Long = 15
        sendPacketService.sendPacket(ioChannel, PingOutDto())

        scheduler.runOnce(runAt = ZonedDateTime.now().plusSeconds(pingPongMaxDelaySeconds)) {
            if (!ioChannel.isActive) { return@runOnce }
            if (!loginHandlerContext.pongReceived(pingedAt)) {
                logger.warn("Client did not respond to ping. Closing channel.")
                ioChannel.disconnect()
            }
        }
    }
}

