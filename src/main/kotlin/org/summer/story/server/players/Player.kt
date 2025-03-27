package org.summer.story.server.players

import io.netty.channel.Channel
import org.slf4j.LoggerFactory
import org.summer.story.net.packet.OutPacket
import org.summer.story.server.TimeService
import org.summer.story.server.dtos.LoginFailedOutDto

class Player(private val timeService: TimeService) {
    companion object {
        private val logger = LoggerFactory.getLogger(Player::class.java)
    }

    private var _ioChannel: Channel? = null
    private var _lastPongAt: Long = 0

    fun updateChannel(channel: Channel) {
        synchronized(this) {
            _ioChannel = channel
        }
    }

    fun isPongReceivedAfter(pingedAt: Long): Boolean = synchronized(this) { _lastPongAt >= pingedAt }

    fun pongReceived() {
        synchronized(this) {
            _lastPongAt = timeService.currentTimeMillis()
        }
    }

    fun isClosed(): Boolean = safelyGetChannel() == null

    fun declareLoginFailed(dto: LoginFailedOutDto) {
        internalSendPacket(dto.toPacket())
    }

    fun close() {
        synchronized(this) { _ioChannel }?.close()
    }

    private fun safelyGetChannel(): Channel? {
        val theChannel: Channel? = synchronized(this) { _ioChannel }
        if (theChannel == null || !theChannel.isActive) {
            return null
        }

        return theChannel
    }

    private fun internalSendPacket(packet: OutPacket) {
        val theChannel: Channel? = safelyGetChannel()
        if (theChannel == null) {
            logger.warn("Channel is null or inactive, cannot send packet: {}", packet.toString())
            return
        }

        theChannel.writeAndFlush(packet)
    }
}