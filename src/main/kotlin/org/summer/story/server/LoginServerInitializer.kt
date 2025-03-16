package org.summer.story.server

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import org.slf4j.LoggerFactory

class LoginServerInitializer : ChannelInitializer<SocketChannel>() {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginServerInitializer::class.java)
    }

    override fun initChannel(ch: SocketChannel) {
        logger.info("Initializing login server channel")
        ch.close()
    }
} 