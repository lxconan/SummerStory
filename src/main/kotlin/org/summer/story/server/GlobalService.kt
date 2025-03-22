package org.summer.story.server

import dev.starry.ktscheduler.scheduler.KtScheduler
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.summer.story.ModuleFactory

object GlobalService {
    private val logger: Logger = LoggerFactory.getLogger(GlobalService::class.java)
    private val koinApp: KoinApplication = koinApplication {
        modules(ModuleFactory.createServerModule())
    }
    private lateinit var globalScope: Koin

    fun start() {
        logger.info("Initializing global service")
        globalScope = koinApp.koin
        updateServiceState(MapleServerState.STARTING)
        globalScope.get<LoginServer>().start()
        globalScope.get<KtScheduler>().start()
        updateServiceState(MapleServerState.RUNNING)
    }

    fun stop() {
        updateServiceState(MapleServerState.SHUTTING_DOWN)
        stopScheduler()
        extracted()
        updateServiceState(MapleServerState.SHUTDOWN)
        globalScope.close()
        logger.info("Global service stopped")
    }

    private fun extracted() {
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