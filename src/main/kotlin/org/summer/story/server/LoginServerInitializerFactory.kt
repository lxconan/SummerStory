package org.summer.story.server

import org.summer.story.config.GlobalConfiguration

class LoginServerInitializerFactory {
    fun createLoginServerInitializer(
        configuration: GlobalConfiguration,
        serverState: GlobalState
    ): LoginServerInitializer {
        return LoginServerInitializer(configuration, serverState)
    }
}