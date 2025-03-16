package org.summer.story.server

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import org.slf4j.LoggerFactory

class LoginServerInitializer : ChannelInitializer<SocketChannel>() {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginServerInitializer::class.java)
    }

    override fun initChannel(ch: SocketChannel) {
        val clientIp: String = ch.remoteAddress().hostString
        logger.debug("Client connected to login server from $clientIp")
        ch.close()
    }
} 