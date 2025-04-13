package org.summer.story.server.login

import dev.starry.ktscheduler.scheduler.KtScheduler
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import kotlinx.coroutines.Dispatchers
import org.slf4j.LoggerFactory
import org.summer.story.net.packet.InPacket
import org.summer.story.server.*
import org.summer.story.server.login.dtos.PingOutDto
import org.summer.story.server.login.game.GameProcessor
import org.summer.story.server.login.game.LoginGameProcessorFactory
import org.summer.story.server.players.Player
import org.summer.story.server.players.PlayerImpl
import java.time.ZonedDateTime

class LoginHandler(
    private val serverState: GlobalState,
    private val timeService: TimeService,
    private val sendPacketService: SendPacketService,
    private val scheduler: KtScheduler,
    private val gameProcessorFactory: LoginGameProcessorFactory,
    private val networkContext: NetworkContext
) : ChannelInboundHandlerAdapter() {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginHandler::class.java)
    }

    private var ioChannel: Channel? = null
    private var player: Player? = null

    override fun channelActive(ctx: ChannelHandlerContext) {
        val channel: Channel = ctx.channel()

        val currentServerState = serverState.serverState
        if (currentServerState != MapleServerState.RUNNING) {
            logger.error("Client connected to login server while server is: {}. The channel will be closed", currentServerState)
            channel.close()
            return
        }

        synchronized(this) {
            if (player == null) {
                val thePlayer: Player = PlayerImpl(timeService, sendPacketService, networkContext)
                thePlayer.updateChannel(channel)
                player = thePlayer
            }

            ioChannel = channel
        }
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        val theIoChannel = synchronized(this) { ioChannel }
        if (theIoChannel == null) { return }
        if (!theIoChannel.isActive) { return }
        pingClientToCheckIdle(theIoChannel)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is InPacket) {
            logger.error("Received a message that is not an InPacket. Cancel reading.")
            return
        }

        val opcode: Short = msg.readShort()
        val gameProcessor: GameProcessor = gameProcessorFactory.getGameProcessor(opcode) ?: return

        try {
            gameProcessor.process(player!!, msg)
        } catch (e: Exception) {
            logger.warn("Error while processing packet with opcode: {}", opcode, e)
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        logger.info("Channel inactive: {}", networkContext.clientIp)

        val thePlayer: Player = synchronized(this) { player } ?: return
        thePlayer.close()
    }

    private fun pingClientToCheckIdle(theIoChannel: Channel) {
        val thePlayer: Player = synchronized(this) { player } ?: return

        val pingedAt: Long = timeService.currentTimeMillis()
        val pingPongMaxDelaySeconds: Long = 15

        sendPacketService.sendPacket(theIoChannel, PingOutDto())

        scheduler.runOnce(runAt = ZonedDateTime.now().plusSeconds(pingPongMaxDelaySeconds), dispatcher = Dispatchers.IO) {
            if (!theIoChannel.isActive) { return@runOnce }
            if (thePlayer.isClosed()) { return@runOnce }
            if (!thePlayer.isPongReceivedAfter(pingedAt)) {
                logger.warn("Client did not respond to ping. Closing channel.")
                thePlayer.close()
            }
        }
    }
}
