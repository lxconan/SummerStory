package org.summer.story.server

abstract class AbstractServer(protected val port: Int) {
    init {
        require(port in 0..65535) { "Port number must be between 0 and 65535" }
    }

    /**
     * Starts the server on the specified port
     */
    abstract fun start()

    /**
     * Stops the server and releases resources
     */
    abstract fun stop()
}