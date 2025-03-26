package org.summer.story

import dev.starry.ktscheduler.scheduler.KtScheduler
import org.koin.core.module.Module
import org.koin.core.module.dsl.*
import org.koin.dsl.module
import org.summer.story.config.loadConfiguration
import org.summer.story.server.*
import org.summer.story.server.game.GameProcessorFactory

object ModuleFactory {
    fun createServerModule() : Module {
        return module {
            single { loadConfiguration() }
            single { KtScheduler() }

            singleOf(::GlobalState)
            singleOf(::LoginServerInitializer)
            singleOf(::LoginServer)
            singleOf(::TimeServiceImpl) { bind<TimeService>() }
            singleOf(::SendPacketServiceImpl) { bind<SendPacketService>() }
            singleOf(::RawPacketFactory)
            singleOf(::GameProcessorFactory)
        }
    }
}