package org.summer.story

import org.koin.dsl.module
import org.summer.story.server.LoginServer
import org.summer.story.server.LoginServerConfiguration
import org.summer.story.server.LoginServerInitializerFactory

object ModuleFactory {
    fun createServerModule() = module {
        single { LoginServerConfiguration() }
        single { LoginServerInitializerFactory(get()) }
        single { LoginServer(get(), get()) }
    }
}