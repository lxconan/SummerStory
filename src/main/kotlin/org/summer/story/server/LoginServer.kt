package org.summer.story.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.LoggerFactory

/**
 * This is the login server for the MapleStory server. It is responsible for handling the login process for the client.
 * There will be only one login server for the entire service.
 * 
 * @param configuration The configuration for the login server.
 */
class LoginServer(
    private val configuration: LoginServerConfiguration,
    private val loginServerInitializerFactory: LoginServerInitializerFactory
) : AbstractServer(configuration.port) {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginServer::class.java)
    }

    private var channel: Channel? = null

    override fun start() {
        logger.info("Starting login server on port $port")
        val parentGroup = NioEventLoopGroup()
        val childGroup = NioEventLoopGroup()
        val bootstrap = ServerBootstrap()
            .group(parentGroup, childGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(loginServerInitializerFactory.createLoginServerInitializer())

        this.channel = bootstrap.bind(port).syncUninterruptibly().channel()
        logger.info("Login server started on port $port")
    }

    override fun stop() {
        if (this.channel != null) {
            this.channel!!.close().syncUninterruptibly()
        }
        logger.info("Login server stopped")
    }
}