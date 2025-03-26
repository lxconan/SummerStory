package org.summer.story.server.players

import io.netty.channel.Channel
import org.summer.story.server.TimeService

class Player(private val timeService: TimeService) {
    private var _ioChannel: Channel? = null
    private var _lastPongAt: Long = 0
    private var _state: PlayerState = PlayerState.DISCONNECTED

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

    fun isDisconnected(): Boolean {
        val state = synchronized(this) { _state }
        return state == PlayerState.DISCONNECTED
    }
}