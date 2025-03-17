package org.summer.story

import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import org.summer.story.server.LoginServer

fun main() {
    startKoin { modules(ModuleFactory.createServerModule()) }
    val rootContainer = getKoin()

    val loginServer: LoginServer = rootContainer.get()
    loginServer.start()
}
