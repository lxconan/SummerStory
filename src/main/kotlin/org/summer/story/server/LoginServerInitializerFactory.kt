package org.summer.story.server

import dev.starry.ktscheduler.scheduler.KtScheduler
import org.summer.story.config.GlobalConfiguration

class LoginServerInitializerFactory(
    private val configuration: GlobalConfiguration,
    private val serverState: GlobalState,
    private val timeService: TimeService,
    private val sendPacketService: SendPacketService,
    private val scheduler: KtScheduler
) {
    fun createLoginServerInitializer(): LoginServerInitializer {
        return LoginServerInitializer(configuration, serverState, timeService, sendPacketService, scheduler)
    }
}