package org.summer.story

import org.summer.story.server.GlobalService

fun main() {
    disableJulLogging()

    Runtime.getRuntime().addShutdownHook(Thread {
        GlobalService.stop()
    })

    GlobalService.start()
}

fun disableJulLogging() {
    val rootLogger = java.util.logging.Logger.getLogger("")
    rootLogger.handlers
        .filterIsInstance<java.util.logging.ConsoleHandler>()
        .forEach(rootLogger::removeHandler)
}
