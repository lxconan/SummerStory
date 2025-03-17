package org.summer.story.server

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.summer.story.net.packet.PacketValidator
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
        loginServer.start()

        // Try to connect using a simple Socket
        val socket = Socket("localhost", testPort)
        assertTrue(socket.isConnected)
        socket.close()
    }

    @Test
    fun `should send correct hello packet on connection`() {
        // Start the server
        loginServer.start()

        // Connect to the server
        val socket = Socket("localhost", testPort)
        assertTrue(socket.isConnected)

        // Read the hello packet
        val inputStream = socket.getInputStream()
        // Hello packet is 16 bytes we create a buffer larger than that to test the packet size correctness.
        val packetBytes = ByteArray(20)
        val bytesRead = inputStream.read(packetBytes)
        
        // Verify packet structure
        assertEquals(16, bytesRead, "Should read exactly 16 bytes")
        PacketValidator.validateHelloPacket(packetBytes)
        socket.close()
    }
}
