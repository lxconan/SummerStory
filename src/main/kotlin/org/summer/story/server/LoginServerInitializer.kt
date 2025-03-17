package org.summer.story.server

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler
import org.slf4j.LoggerFactory
import org.summer.story.net.encryption.IvPair
import org.summer.story.net.packet.PacketFactory

class LoginServerInitializer : ChannelInitializer<SocketChannel>() {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginServerInitializer::class.java)
        private const val IDLE_STATE_SECONDS = 30
    }

    override fun initChannel(ch: SocketChannel) {
        val clientIp: String = ch.remoteAddress().hostString
        logger.info("Client connected to login server from $clientIp")
        val iv = IvPair()
        writeInitialUnencryptedHelloPacket(ch, iv)
        setupHandlers(ch.pipeline(), iv)
    }

    private fun setupHandlers(pipeline: ChannelPipeline, iv: IvPair) {
        /**
         * Adds an IdleStateHandler to the pipeline to detect idle connections if there is no activity for a certain period of time.
         * This is useful for detecting dead connections and closing them. A custom handler can be added to the pipeline to handle
         * the event (the Client handler).
         */
        pipeline.addLast("IdleStateHandler", IdleStateHandler(0, 0, IDLE_STATE_SECONDS))
    }

    private fun writeInitialUnencryptedHelloPacket(ch: SocketChannel, iv: IvPair) {
        val buffer = Unpooled.wrappedBuffer(
            PacketFactory.createHello(
                ServerMetadata.ServerVersion,
                iv.send,
                iv.receive
            ).getBytes()
        )

        ch.writeAndFlush(buffer)
    }
} 