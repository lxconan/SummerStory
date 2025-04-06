package org.summer.story

import dev.starry.ktscheduler.scheduler.KtScheduler
import org.koin.core.module.Module
import org.koin.core.module.dsl.*
import org.koin.dsl.module
import org.summer.story.config.loadConfiguration
import org.summer.story.data.AccountRepository
import org.summer.story.data.MapleDataSource
import org.summer.story.data.MapleDataSourceImpl
import org.summer.story.net.encryption.HashAlgorithm
import org.summer.story.server.*
import org.summer.story.server.game.*
import org.summer.story.server.worlds.WorldManager

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
            singleOf(::WorldManager)

            singleOf(::MapleDataSourceImpl) { bind<MapleDataSource>() }

            singleOf(::KeepAliveProcessor) { bind<GameProcessor>() }
            singleOf(::LoginPasswordProcessor) { bind<GameProcessor>() }
            singleOf(::ServerListRequestProcessor) { bind<GameProcessor>() }
            singleOf(::ServerStatusRequestProcessor) { bind<GameProcessor>() }
            singleOf(::ViewAllCharactersRequestProcessor) { bind<GameProcessor>() }

            single { GameProcessorFactory(getAll()) }

            singleOf(::AccountRepository)
            singleOf(::HashAlgorithm)
        }
    }
}