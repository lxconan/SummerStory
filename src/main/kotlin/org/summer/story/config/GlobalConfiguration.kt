package org.summer.story.config

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.nio.charset.Charset

@Serializable
data class GlobalConfiguration(
    val loginServer: LoginServerConfiguration = LoginServerConfiguration(),
    val debug: DebugConfiguration = DebugConfiguration(),
    val database: DatabaseConfiguration = DatabaseConfiguration(),
    val packet: PacketConfiguration = PacketConfiguration()
)

@Serializable
data class PacketConfiguration(
    val charset: String = "US_ASCII"
) {
    companion object {
        private fun getCharset(charset: String): Charset {
            return if (charset == "US_ASCII") {
                Charsets.US_ASCII
            } else {
                throw IllegalArgumentException("Unsupported charset: $charset")
            }
        }
    }

    @Transient
    val charsetObject: Charset = getCharset(charset)
}

@Serializable data class DatabaseConfiguration(
    val jdbcUrl: String = "jdbc:postgresql://localhost:5432/maplestory",
    val username: String = "postgres",
    val password: String = "",
    val maximumPoolSize: Int = 10
)

@Serializable
data class DebugConfiguration(
    val recordPacket: Boolean = false
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