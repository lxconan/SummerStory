package org.summer.story.server

import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.summer.story.net.packet.Packet

interface SendPacketService {
    fun sendPacket(channel: Channel, packet: Packet): ChannelFuture
}

class SendPacketServiceImpl : SendPacketService {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SendPacketServiceImpl::class.java)
    }

    override fun sendPacket(channel: Channel, packet: Packet): ChannelFuture {
        // The writeAndFlush method is thread-safe per channel so there is no need to add any synchronization.
        return channel.writeAndFlush(packet)
    }
}