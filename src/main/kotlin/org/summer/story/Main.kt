package org.summer.story

import org.summer.story.server.LoginServer

fun main() {
    val loginServer = LoginServer(8484)
    loginServer.start()
}
