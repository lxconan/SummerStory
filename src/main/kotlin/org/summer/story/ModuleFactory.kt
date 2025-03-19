package org.summer.story

import org.koin.dsl.module
import org.summer.story.config.GlobalConfiguration
import org.summer.story.config.loadConfiguration
import org.summer.story.server.LoginServer
import org.summer.story.server.LoginServerInitializerFactory

object ModuleFactory {
    fun createServerModule() = module {
        single { loadConfiguration() }
        single { get<GlobalConfiguration>().loginServer }
        single { LoginServerInitializerFactory(get()) }
        single { LoginServer(get(), get()) }
    }
}