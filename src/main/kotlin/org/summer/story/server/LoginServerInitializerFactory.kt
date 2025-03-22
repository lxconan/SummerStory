package org.summer.story.server

import org.summer.story.config.GlobalConfiguration

class LoginServerInitializerFactory(
    private val configuration: GlobalConfiguration,
    private val serverState: GlobalState,
    private val timeService: TimeService,
    private val sendPacketService: SendPacketService
) {
    fun createLoginServerInitializer(): LoginServerInitializer {
        return LoginServerInitializer(configuration, serverState, timeService, sendPacketService)
    }
}