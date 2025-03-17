package org.summer.story.server

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import org.slf4j.LoggerFactory
import org.summer.story.net.encryption.IvPair
import org.summer.story.net.packet.PacketFactory

class LoginServerInitializer : ChannelInitializer<SocketChannel>() {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginServerInitializer::class.java)
    }

    override fun initChannel(ch: SocketChannel) {
        val clientIp: String = ch.remoteAddress().hostString
        logger.debug("Client connected to login server from $clientIp")
        val iv = IvPair()
        writeInitialUnencryptedHelloPacket(ch, iv)
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