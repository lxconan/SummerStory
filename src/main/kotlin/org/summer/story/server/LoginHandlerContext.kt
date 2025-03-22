package org.summer.story.server

class LoginHandlerContext {
    private var lastPongAt: Long = 0

    fun updateLastPong() {
        lastPongAt = System.currentTimeMillis()
    }

    fun pongReceived(lastPingAt: Long): Boolean = lastPongAt >= lastPingAt
}