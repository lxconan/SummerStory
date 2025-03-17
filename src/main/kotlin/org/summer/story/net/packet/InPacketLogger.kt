package org.summer.story.net.packet

import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import org.slf4j.LoggerFactory
import io.netty.channel.ChannelInboundHandlerAdapter

@Sharable
class InPacketLogger : ChannelInboundHandlerAdapter(), PacketLogger {
    companion object {
        private val logger = LoggerFactory.getLogger(InPacketLogger::class.java)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        TODO("Not yet implemented")
    }

    override fun log(packet: Packet) {
        TODO("Not yet implemented")
    }
}
