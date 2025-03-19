package org.summer.story.net.encryption

import org.junit.jupiter.api.Test
import org.summer.story.server.ServerMetadata
import kotlin.test.assertTrue

class MapleAesOfbTest {
    @Test
    fun `should accept valid header`() {
        // Given the login packet header (we extracted from a real packet)
        val loginPacketHeader = -1171483348
        // The current IV. Please note that each time a packet is decoded, the IV is updated.
        val currentIv = byteArrayOf(0xE4.toByte(), 0x91.toByte(), 0xE9.toByte(), 0x2C)

        val mapleAesOfb = MapleAesOfb(currentIv, ServerMetadata.SERVER_VERSION.toShort())
        assertTrue(mapleAesOfb.isValidHeader(loginPacketHeader), "This header should be valid")
    }
}