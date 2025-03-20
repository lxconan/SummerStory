package org.summer.story

import org.koin.core.module.Module
import org.koin.dsl.module
import org.summer.story.config.loadConfiguration
import org.summer.story.server.GlobalState
import org.summer.story.server.LoginServer
import org.summer.story.server.LoginServerInitializerFactory

object ModuleFactory {
    fun createServerModule() : Module {
        return module {
            single { GlobalState() }
            single { loadConfiguration() }
            single { LoginServerInitializerFactory() }
            single { LoginServer(get(), get()) }
        }
    }
}