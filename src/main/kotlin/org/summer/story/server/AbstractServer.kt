package org.summer.story.server

abstract class AbstractServer(protected val port: Int) {
    init {
        require(port in 0..65535) { "Port number must be between 0 and 65535" }
    }

    abstract fun start()
    abstract fun stop()
}