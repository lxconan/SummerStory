package org.summer.story.net.encryption

import org.junit.jupiter.api.Test
import org.summer.story.server.ServerMetadata
import kotlin.test.assertTrue

class MapleAesOfbTest {
    @Test
    fun `should check invalid header`() {
        val loginPacketHeader = -1171483348
        val mapleAesOfb = MapleAesOfb(byteArrayOf(0xE4.toByte(), 0x91.toByte(), 0xE9.toByte(), 0x2C) , ServerMetadata.ServerVersion.toShort())
        assertTrue(mapleAesOfb.isValidHeader(loginPacketHeader), "This header should be valid")
    }
}