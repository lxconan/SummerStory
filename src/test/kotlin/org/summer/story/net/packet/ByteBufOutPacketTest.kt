package org.summer.story.net.packet

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.awt.Point
import java.nio.charset.StandardCharsets

class ByteBufOutPacketTest {
    private lateinit var packet: ByteBufOutPacket

    @BeforeEach
    fun setup() {
        packet = ByteBufOutPacket()
    }

    @Test
    fun `should write byte value`() {
        packet.writeByte(0x12.toByte())
        assertArrayEquals(byteArrayOf(0x12), packet.getBytes())
    }

    @Test
    fun `should write least significant 8-bit of integer`() {
        packet.writeByte(0x1234)
        assertArrayEquals(byteArrayOf(0x34), packet.getBytes())
    }

    @Test
    fun `should write byte array`() {
        val bytes = byteArrayOf(1, 2, 3, 4)
        packet.writeBytes(bytes)
        assertArrayEquals(bytes, packet.getBytes())
    }

    @Test
    fun `should write short in little-endian format`() {
        packet.writeShort(0x1234)
        assertArrayEquals(byteArrayOf(0x34, 0x12), packet.getBytes()) // Little-endian
    }

    @Test
    fun `should write int in little-endian format`() {
        packet.writeInt(0x12345678)
        assertArrayEquals(
            byteArrayOf(0x78, 0x56, 0x34, 0x12), // Little-endian
            packet.getBytes()
        )
    }

    @Test
    fun `should write long in little-endian format`() {
        packet.writeLong(0x1234567890ABCDEF)
        assertArrayEquals(
            byteArrayOf(0xEF.toByte(), 0xCD.toByte(), 0xAB.toByte(), 0x90.toByte(),
                       0x78, 0x56, 0x34, 0x12), // Little-endian
            packet.getBytes()
        )
    }

    @Test
    fun `should write boolean values as 1 or 0`() {
        packet.writeBool(true)
        packet.writeBool(false)
        assertArrayEquals(byteArrayOf(1, 0), packet.getBytes())
    }

    @Test
    fun `should write string with length prefix`() {
        val testString = "Hello"
        packet.writeString(testString, StandardCharsets.UTF_8)
        
        val expected = ByteArray(7) // 2 bytes for length + 5 bytes for "Hello"
        expected[0] = 5 // String length (little-endian short)
        expected[1] = 0
        System.arraycopy(testString.toByteArray(StandardCharsets.UTF_8), 0, expected, 2, 5)
        
        assertArrayEquals(expected, packet.getBytes())
    }

    @Test
    fun `should throw exception when string is too long for writeString`() {
        val longString = "a".repeat(Short.MAX_VALUE + 1)
        assertThrows<IllegalArgumentException> {
            packet.writeString(longString, StandardCharsets.UTF_8)
        }
    }

    @Test
    fun `should write fixed string without length prefix`() {
        val testString = "Hello"
        packet.writeFixedString(testString, StandardCharsets.UTF_8)
        assertArrayEquals(testString.toByteArray(StandardCharsets.UTF_8), packet.getBytes())
    }

    @Test
    fun `should throw exception when string is too long for writeFixedString`() {
        val longString = "a".repeat(Short.MAX_VALUE + 1)
        assertThrows<IllegalArgumentException> {
            packet.writeFixedString(longString, StandardCharsets.UTF_8)
        }
    }

    @Test
    fun `should write point coordinates as little-endian shorts`() {
        val point = Point(300, 400)
        packet.writePos(point)
        assertArrayEquals(
            byteArrayOf(0x2C, 0x01, 0x90.toByte(), 0x01), // 300, 400 in little-endian shorts
            packet.getBytes()
        )
    }

    @Test
    fun `should add specified number of zero bytes`() {
        packet.skip(3)
        assertArrayEquals(ByteArray(3), packet.getBytes())
    }

    @Test
    fun `should correctly combine multiple writes`() {
        packet.writeByte(1)
        packet.writeShort(2)
        packet.writeInt(3)
        packet.writeBool(true)
        
        val expected = byteArrayOf(
            1,           // byte
            2, 0,       // short (little-endian)
            3, 0, 0, 0, // int (little-endian)
            1           // bool (true)
        )
        assertArrayEquals(expected, packet.getBytes())
    }

    @Test
    fun `should be equal when containing same data`() {
        val packet1 = ByteBufOutPacket()
        val packet2 = ByteBufOutPacket()
        
        packet1.writeInt(12345)
        packet2.writeInt(12345)
        
        assertEquals(packet1, packet2)
        assertEquals(packet1.hashCode(), packet2.hashCode())
    }
} 