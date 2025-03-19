package org.summer.story.net.packet

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.summer.story.server.ServerMetadata

object PacketValidator {
    fun validateHelloPacket(packetBytes: ByteArray) {
        // First validate packet size
        assertTrue(packetBytes.size >= 16, "Hello packet must be at least 16 bytes, but was ${packetBytes.size}")

        // Check packet header (0x0E - hello packet identifier)
        with(packetBytes) {
            // Header validation
            assertEquals(0x0Eu, this[0].toUByte(), "Packet identifier should be 0x0E")
            assertEquals(0x00u, this[1].toUByte(), "Second byte should be 0x00")

            // Maple version validation
            val mapleVersion = (this[2].toUByte().toInt()) or (this[3].toUByte().toInt() shl 8)
            assertEquals(
                ServerMetadata.SERVER_VERSION,
                mapleVersion,
                "Invalid maple version: expected ${ServerMetadata.SERVER_VERSION}, but was $mapleVersion"
            )
            
            // Protocol data validation
            assertEquals(1u, this[4].toUByte(), "Protocol byte 1 mismatch")
            assertEquals(0u, this[5].toUByte(), "Protocol byte 2 mismatch")
            assertEquals(49u, this[6].toUByte(), "Protocol byte 3 mismatch")

            // Receive IV validation (bytes 7-10)
            validateReceiveIv(this, 7)
            
            // Send IV validation (bytes 11-14)
            validateSendIv(this, 11)

            // Footer validation
            assertEquals(8u, this[15].toUByte(), "Footer byte mismatch")
        }
    }

    fun validateSendIv(packet: ByteArray, offset: Int) {
        assertEquals(82u, packet[offset].toUByte(), "Send IV byte 1 mismatch")
        assertEquals(48u, packet[offset + 1].toUByte(), "Send IV byte 2 mismatch")
        assertEquals(120u, packet[offset + 2].toUByte(), "Send IV byte 3 mismatch")
    }

    fun validateReceiveIv(packet: ByteArray, offset: Int) {
        assertEquals(70u, packet[offset].toUByte(), "Receive IV byte 1 mismatch")
        assertEquals(114u, packet[offset + 1].toUByte(), "Receive IV byte 2 mismatch")
        assertEquals(122u, packet[offset + 2].toUByte(), "Receive IV byte 3 mismatch")
    }
}