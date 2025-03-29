package org.summer.story.server.players

import io.netty.channel.Channel
import org.slf4j.LoggerFactory
import org.summer.story.server.NetworkContext
import org.summer.story.server.SendPacketService
import org.summer.story.server.TimeService
import org.summer.story.server.dtos.OutDto

class Player(
    private val timeService: TimeService,
    private val sendPacketService: SendPacketService,
    networkContext: NetworkContext
) {
    companion object {
        private val logger = LoggerFactory.getLogger(Player::class.java)
    }

    private var _ioChannel: Channel? = null
    private var _lastPongAt: Long = 0

    val clientIp: String
    var accountContext: AccountContext? = null

    init {
        require(networkContext.isValid()) { "Network context is invalid" }
        clientIp = networkContext.clientIp!!
    }

    fun updateChannel(channel: Channel) {
        synchronized(this) {
            _ioChannel = channel
        }
    }

    fun isPongReceivedAfter(pingedAt: Long): Boolean = synchronized(this) { _lastPongAt >= pingedAt }

    fun pongReceived() = synchronized(this) {
        _lastPongAt = timeService.currentTimeMillis()
    }

    fun isClosed(): Boolean = safelyGetChannel() == null

    fun close() { synchronized(this) { _ioChannel }?.close() }

    private fun safelyGetChannel(): Channel? {
        val theChannel: Channel? = synchronized(this) { _ioChannel }
        if (theChannel == null || !theChannel.isActive) {
            return null
        }

        return theChannel
    }

    fun respond(dto: OutDto) {
        val theChannel: Channel? = safelyGetChannel()
        if (theChannel == null) {
            logger.warn("Channel is null or inactive, cannot send packet: {}", dto.toString())
            return
        }

        sendPacketService.sendPacket(theChannel, dto)
    }
}