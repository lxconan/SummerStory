package org.summer.story

import dev.starry.ktscheduler.scheduler.KtScheduler
import org.koin.core.module.Module
import org.koin.core.module.dsl.*
import org.koin.dsl.module
import org.summer.story.config.GlobalConfiguration
import org.summer.story.config.MapleMetadata
import org.summer.story.config.loadEmbeddedResource
import org.summer.story.data.AccountRepository
import org.summer.story.data.CharacterRepository
import org.summer.story.data.MapleDataSource
import org.summer.story.data.MapleDataSourceImpl
import org.summer.story.net.encryption.HashAlgorithm
import org.summer.story.server.*
import org.summer.story.server.login.game.service.CharacterNameValidationService
import org.summer.story.server.login.LoginServer
import org.summer.story.server.login.LoginServerInitializer
import org.summer.story.server.login.game.*
import org.summer.story.server.worlds.WorldManager

object ModuleFactory {
    fun createServerModule() : Module {
        return module {
            single { loadEmbeddedResource<GlobalConfiguration>("/config.yaml") { GlobalConfiguration() } }
            singleOf(::MapleMetadata)
            single { KtScheduler() }

            singleOf(::GlobalState)

            singleOf(::LoginServerInitializer)
            singleOf(::LoginServer)
            singleOf(::TimeServiceImpl) { bind<TimeService>() }
            singleOf(::SendPacketServiceImpl) { bind<SendPacketService>() }
            singleOf(::RawPacketFactory)
            singleOf(::WorldManager)

            singleOf(::MapleDataSourceImpl) { bind<MapleDataSource>() }

            singleOf(::KeepAliveProcessor) { bind<LoginServerGameProcessor>() }
            singleOf(::LoginPasswordProcessor) { bind<LoginServerGameProcessor>() }
            singleOf(::ServerListRequestProcessor) { bind<LoginServerGameProcessor>() }
            singleOf(::ServerStatusRequestProcessor) { bind<LoginServerGameProcessor>() }
            singleOf(::ViewAllCharactersRequestProcessor) { bind<LoginServerGameProcessor>() }
            singleOf(::CharacterListRequestProcessor) { bind<LoginServerGameProcessor>() }
            singleOf(::CheckCharacterNameRequestProcessor) { bind<LoginServerGameProcessor>() }
            singleOf(::CreateCharacterGameProcessor) { bind<LoginServerGameProcessor>() }

            single { LoginGameProcessorFactory(getAll()) }

            singleOf(::CharacterNameValidationService)

            singleOf(::AccountRepository)
            singleOf(::CharacterRepository)
            singleOf(::HashAlgorithm)
        }
    }
}