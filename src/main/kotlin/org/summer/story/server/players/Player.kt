package org.summer.story.server.players

import io.netty.channel.Channel
import org.summer.story.server.TimeService

class Player(private val timeService: TimeService) {
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

    fun isClosed(): Boolean {
        val channel:Channel? = synchronized(this) { _ioChannel }
        return channel == null || !channel.isActive
    }

    fun close() {
        synchronized(this) { _ioChannel }?.close()
    }
}