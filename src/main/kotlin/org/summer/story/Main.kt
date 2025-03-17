package org.summer.story

import org.summer.story.server.LoginServer
import org.summer.story.server.LoginServerConfiguration

fun main() {
    val loginServer = LoginServer(LoginServerConfiguration())
    loginServer.start()
}
