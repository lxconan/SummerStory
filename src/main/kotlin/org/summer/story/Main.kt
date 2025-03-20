package org.summer.story

import org.koin.dsl.koinApplication
import org.summer.story.server.LoginServer

fun main() {
    val koinApp = koinApplication {
        modules(ModuleFactory.createServerModule())
    }

    val loginServer: LoginServer = koinApp.koin.get()
    loginServer.start()
}
