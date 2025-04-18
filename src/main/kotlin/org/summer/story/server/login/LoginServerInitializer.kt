package org.summer.story.server.login

import dev.starry.ktscheduler.scheduler.KtScheduler
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler
import org.slf4j.LoggerFactory
import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.encryption.ClientCyphers
import org.summer.story.net.encryption.IvPair
import org.summer.story.server.*
import org.summer.story.server.login.game.LoginGameProcessorFactory

class LoginServerInitializer(
    private val configuration: GlobalConfiguration,
    private val serverState: GlobalState,
    private val timeService: TimeService,
    private val sendPacketService: SendPacketService,
    private val scheduler: KtScheduler,
    private val rawPacketFactory: RawPacketFactory,
    private val gameProcessorFactory: LoginGameProcessorFactory
) : ChannelInitializer<SocketChannel>() {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginServerInitializer::class.java)
    }

    override fun initChannel(ch: SocketChannel) {
        val networkContext = NetworkContext(ch)

        if (!networkContext.isValid()) {
            logger.error("Client connected to login server with null IP. The channel will be closed.")
            ch.close()
            return
        }

        logger.info("Client connected to login server from ${networkContext.clientIp}")
        val iv = IvPair()
        writeInitialUnencryptedHelloPacket(ch, iv)
        setupHandlers(ch.pipeline(), iv, networkContext)
    }

    private fun setupHandlers(pipeline: ChannelPipeline, iv: IvPair, networkContext: NetworkContext) {
        /**
         * Adds an IdleStateHandler to the pipeline to detect idle connections if there is no activity for a certain period of time.
         * This is useful for detecting dead connections and closing them. A custom handler can be added to the pipeline to handle
         * the event (the Client handler).
         */
        pipeline.addLast(
            "IdleStateHandler",
            IdleStateHandler(0, 0, configuration.loginServer.idleTimeSeconds))
        pipeline.addLast("PacketCodec", PacketCodec(ClientCyphers.create(iv), configuration))
        pipeline.addLast(
            "LoginHandler",
            LoginHandler(
                serverState,
                timeService,
                sendPacketService,
                scheduler,
                gameProcessorFactory,
                networkContext
            )
        )
    }

    private fun writeInitialUnencryptedHelloPacket(ch: SocketChannel, iv: IvPair) {
        val buffer = Unpooled.wrappedBuffer(
            rawPacketFactory.createHello(
                ServerMetadata.SERVER_VERSION,
                iv.send,
                iv.receive
            ).getBytes()
        )

        ch.writeAndFlush(buffer)
    }
} 