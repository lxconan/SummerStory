package org.summer.story

import org.summer.story.server.GlobalService

fun main() {
    Runtime.getRuntime().addShutdownHook(Thread {
        GlobalService.stop()
    })

    GlobalService.start()
}
