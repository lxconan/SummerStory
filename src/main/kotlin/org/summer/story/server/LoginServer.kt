package org.summer.story.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.LoggerFactory
import org.summer.story.config.GlobalConfiguration

/**
 * This is the login server for the MapleStory server. It is responsible for handling the login process for the client.
 * There will be only one login server for the entire service.
 */
class LoginServer(
    private val configuration: GlobalConfiguration,
    private val loginServerInitializerFactory: LoginServerInitializerFactory
) {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginServer::class.java)
    }

    private var channel: Channel? = null

    fun start() {
        val port: Int = configuration.loginServer.port

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

    fun stop() {
        if (this.channel != null) {
            this.channel!!.close().syncUninterruptibly()
        }
        logger.info("Login server stopped")
    }
}