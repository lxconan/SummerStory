package org.summer.story.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.nio.charset.Charset

@Serializable
class GlobalConfiguration(
    val loginServer: LoginServerConfiguration = LoginServerConfiguration(),
    val debug: DebugConfiguration = DebugConfiguration(),
    val database: DatabaseConfiguration = DatabaseConfiguration(),
    val packet: PacketConfiguration = PacketConfiguration()
)

@Serializable
class PacketConfiguration(
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

@Serializable
class DatabaseConfiguration(
    val jdbcUrl: String = "jdbc:postgresql://localhost:5432/maplestory",
    val username: String = "postgres",
    val password: String = "",
    val maximumPoolSize: Int = 10
)

@Serializable
class DebugConfiguration(
    val recordPacket: Boolean = false
)

@Serializable
class LoginServerConfiguration(
    val port: Int = 8484,
    val idleTimeSeconds: Int = 30
)
