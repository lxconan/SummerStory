package org.summer.story.server

import dev.starry.ktscheduler.scheduler.KtScheduler
import org.flywaydb.core.Flyway
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.summer.story.ModuleFactory
import org.summer.story.config.GlobalConfiguration
import org.summer.story.server.worlds.WorldDefinitions

object GlobalService {
    private val logger: Logger = LoggerFactory.getLogger(GlobalService::class.java)
    private val koinApp: KoinApplication = koinApplication {
        modules(ModuleFactory.createServerModule())
    }
    private lateinit var globalScope: Koin

    fun start() {
        logger.info("Initializing global service")
        globalScope = koinApp.koin
        runDatabaseMigration()
        updateServiceState(MapleServerState.STARTING)
        validateConfiguration()
        globalScope.get<LoginServer>().start()
        globalScope.get<KtScheduler>().start()
        updateServiceState(MapleServerState.RUNNING)
    }

    private fun validateConfiguration() {
        val configuration = globalScope.get<GlobalConfiguration>()
        require(configuration.world.channelCount > 0 && configuration.world.channelCount <= WorldDefinitions.maximumChannels) {
            "Invalid channel count: ${configuration.world.channelCount}"
        }
    }

    fun stop() {
        updateServiceState(MapleServerState.SHUTTING_DOWN)
        stopScheduler()
        stopLoginServer()
        updateServiceState(MapleServerState.SHUTDOWN)
        globalScope.close()
        logger.info("Global service stopped")
    }

    private fun runDatabaseMigration() {
        val configuration = globalScope.get<GlobalConfiguration>()
        val dataSource = PGSimpleDataSource()
        dataSource.setUrl("${configuration.database.jdbcUrl}?user=${configuration.database.username}&password=${configuration.database.password}")
        Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .load()
            .migrate()
    }

    private fun stopLoginServer() {
        try {
            globalScope.get<LoginServer>().stop()
        } catch (e: Exception) {
            logger.error("Error while stopping login server", e)
        }
    }

    private fun stopScheduler() {
        try {
            globalScope.get<KtScheduler>().shutdown()
        } catch (e: Exception) {
            logger.error("Error while stopping scheduler", e)
        }
    }

    private fun updateServiceState(newState: MapleServerState) {
        val globalState = globalScope.get<GlobalState>()
        globalState.serverState = newState
        logger.info("Service state updated to {}", newState)
    }
}