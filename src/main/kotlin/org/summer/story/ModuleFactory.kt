package org.summer.story

import dev.starry.ktscheduler.scheduler.KtScheduler
import org.koin.core.module.Module
import org.koin.core.module.dsl.*
import org.koin.dsl.module
import org.summer.story.config.loadConfiguration
import org.summer.story.data.MapleDataSource
import org.summer.story.data.MapleDataSourceImpl
import org.summer.story.server.*
import org.summer.story.server.game.GameProcessor
import org.summer.story.server.game.GameProcessorFactory
import org.summer.story.server.game.KeepAliveProcessor
import org.summer.story.server.game.LoginPasswordProcessor

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

            singleOf(::KeepAliveProcessor) { bind<GameProcessor>() }
            singleOf(::LoginPasswordProcessor) { bind<GameProcessor>() }
            singleOf(::MapleDataSourceImpl) { bind<MapleDataSource>() }

            single { GameProcessorFactory(getAll()) }
        }
    }
}