package org.summer.story.server

import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.summer.story.server.dtos.OutDto

interface SendPacketService {
    fun sendPacket(channel: Channel, dto: OutDto): ChannelFuture
}

class SendPacketServiceImpl : SendPacketService {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SendPacketServiceImpl::class.java)
    }

    override fun sendPacket(channel: Channel, dto: OutDto): ChannelFuture {
        return channel.writeAndFlush(dto.toPacket())
    }
}