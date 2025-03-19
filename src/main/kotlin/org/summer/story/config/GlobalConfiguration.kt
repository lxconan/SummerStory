package org.summer.story.config

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.serialization.Serializable

@Serializable
data class GlobalConfiguration(
    val loginServer: LoginServerConfiguration = LoginServerConfiguration()
)

@Serializable
data class LoginServerConfiguration(
    val port: Int = 8484,
    val idleTimeSeconds: Int = 30
)

fun loadConfiguration(): GlobalConfiguration {
    GlobalConfiguration::class.java.getResourceAsStream("/config.yaml").use { inputStream ->
        when (inputStream) {
            null -> return GlobalConfiguration()
            else -> return Yaml.default.decodeFromStream(inputStream)
        }
    }
}