package org.summer.story.server

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.summer.story.ModuleFactory

object GlobalService {
    private val logger: Logger = LoggerFactory.getLogger(GlobalService::class.java)
    private val koinApp: KoinApplication = koinApplication{
        modules(ModuleFactory.createServerModule())
    }
    private lateinit var globalScope: Koin

    fun start() {
        logger.info("Initializing global service")
        globalScope = koinApp.koin
        updateServiceState(MapleServerState.STARTING)

        val loginServer: LoginServer = globalScope.get()
        loginServer.start()

        updateServiceState(MapleServerState.RUNNING)
    }

    private fun updateServiceState(newState: MapleServerState) {
        val globalState = globalScope.get<GlobalState>()
        globalState.serverState = newState
        logger.info("Service state updated to {}", newState)
    }
}