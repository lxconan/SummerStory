package org.summer.story

import dev.starry.ktscheduler.scheduler.KtScheduler
import org.koin.core.module.Module
import org.koin.dsl.module
import org.summer.story.config.loadConfiguration
import org.summer.story.server.*

object ModuleFactory {
    fun createServerModule() : Module {
        return module {
            single { GlobalState() }
            single { loadConfiguration() }
            single { LoginServerInitializerFactory(get(), get(), get(), get(), get()) }
            single { LoginServer(get(), get()) }
            single<TimeService> { TimeServiceImpl() }
            single<SendPacketService> { SendPacketServiceImpl() }
            single { KtScheduler() }
        }
    }
}