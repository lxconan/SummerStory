package org.summer.story.server

import org.summer.story.config.LoginServerConfiguration

class LoginServerInitializerFactory(private val loginServerConfiguration: LoginServerConfiguration) {
    fun createLoginServerInitializer(): LoginServerInitializer {
        return LoginServerInitializer(loginServerConfiguration)
    }
}