package org.summer.story.server

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.Socket

class LoginServerTest {
    private lateinit var loginServer: LoginServer
    private val testPort = 8484

    @BeforeEach
    fun setup() {
        loginServer = LoginServer(testPort)
    }

    @AfterEach
    fun tearDown() {
        loginServer.stop()
    }

    @Test
    fun `should starts and accepts connections`() {
        // Start the server
        loginServer.start()

        // Try to connect using a simple Socket
        val socket = Socket("localhost", testPort)
        assertTrue(socket.isConnected)
        socket.close()
    }
}
