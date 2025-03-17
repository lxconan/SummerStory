package org.summer.story.server

class LoginServerInitializerFactory(private val loginServerConfiguration: LoginServerConfiguration) {
    fun createLoginServerInitializer(): LoginServerInitializer {
        return LoginServerInitializer(loginServerConfiguration)
    }
}