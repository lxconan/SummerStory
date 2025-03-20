package org.summer.story.server

import org.summer.story.config.GlobalConfiguration

class LoginServerInitializerFactory {
    fun createLoginServerInitializer(configuration: GlobalConfiguration): LoginServerInitializer {
        return LoginServerInitializer(configuration)
    }
}