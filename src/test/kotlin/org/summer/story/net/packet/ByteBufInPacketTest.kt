package org.summer.story.net.packet

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Point
import java.nio.charset.StandardCharsets
import kotlin.test.*

class ByteBufInPacketTest {
    private lateinit var byteBuf: ByteBuf
    private lateinit var packet: ByteBufInPacket

    @BeforeEach
    fun setUp() {
        byteBuf = Unpooled.buffer()
        packet = ByteBufInPacket(byteBuf)
    }

    @Test
    fun `should read byte value`() {
        // Arrange
        val testByte: Byte = 42
        byteBuf.writeByte(testByte.toInt())

        // Act
        val result = packet.readByte()

        // Assert
        assertEquals(testByte, result)
        assertEquals(1, byteBuf.readerIndex())
    }

    @Test
    fun `should read unsigned byte value`() {
        // Arrange
        val testByte: Short = 200
        byteBuf.writeByte(testByte.toInt())

        // Act
        val result = packet.readUnsignedByte()

        // Assert
        assertEquals(testByte, result)
        assertEquals(1, byteBuf.readerIndex())
    }

    @Test
    fun `should read short value in little-endian format`() {
        // Arrange
        val testShort: Short = 12345
        byteBuf.writeShortLE(testShort.toInt())

        // Act
        val result = packet.readShort()

        // Assert
        assertEquals(testShort, result)
        assertEquals(2, byteBuf.readerIndex())
    }

    @Test
    fun `should read int value in little-endian format`() {
        // Arrange
        val testInt = 123456789
        byteBuf.writeIntLE(testInt)

        // Act
        val result = packet.readInt()

        // Assert
        assertEquals(testInt, result)
        assertEquals(4, byteBuf.readerIndex())
    }

    @Test
    fun `should read long value in little-endian format`() {
        // Arrange
        val testLong = 1234567890123456789L
        byteBuf.writeLongLE(testLong)

        // Act
        val result = packet.readLong()

        // Assert
        assertEquals(testLong, result)
        assertEquals(8, byteBuf.readerIndex())
    }

    @Test
    fun `should read position as two little-endian shorts`() {
        // Arrange
        val x: Short = 100
        val y: Short = 200
        byteBuf.writeShortLE(x.toInt())
        byteBuf.writeShortLE(y.toInt())

        // Act
        val result = packet.readPos()

        // Assert
        assertEquals(Point(x.toInt(), y.toInt()), result)
        assertEquals(4, byteBuf.readerIndex())
    }

    @Test
    fun `should read string with length prefix`() {
        // Arrange
        val testString = "Hello, World!"
        val bytes = testString.toByteArray(StandardCharsets.UTF_8)
        byteBuf.writeShortLE(bytes.size)
        byteBuf.writeBytes(bytes)

        // Act
        val result = packet.readString(StandardCharsets.UTF_8)

        // Assert
        assertEquals(testString, result)
        assertEquals(bytes.size + 2, byteBuf.readerIndex())
    }

    @Test
    fun `should throw exception when reading string with negative length`() {
        // Arrange
        byteBuf.writeShortLE(-1)

        // Act & Assert
        assertFailsWith<IllegalArgumentException> {
            packet.readString(StandardCharsets.UTF_8)
        }
    }

    @Test
    fun `should read byte array`() {
        // Arrange
        val testBytes = byteArrayOf(1, 2, 3, 4, 5)
        byteBuf.writeBytes(testBytes)

        // Act
        val result = packet.readBytes(testBytes.size)

        // Assert
        assertContentEquals(testBytes, result)
        assertEquals(testBytes.size, byteBuf.readerIndex())
    }

    @Test
    fun `should throw exception when reading bytes with negative count`() {
        assertFailsWith<IllegalArgumentException> {
            packet.readBytes(-1)
        }
    }

    @Test
    fun `should throw exception when reading beyond available bytes`() {
        assertFailsWith<IllegalArgumentException> {
            packet.readBytes(100)
        }
    }

    @Test
    fun `should skip specified number of bytes`() {
        // Arrange
        byteBuf.writeBytes(ByteArray(10))

        // Act
        packet.skip(5)

        // Assert
        assertEquals(5, byteBuf.readerIndex())
    }

    @Test
    fun `should throw exception when skipping negative number of bytes`() {
        assertFailsWith<IllegalArgumentException> {
            packet.skip(-1)
        }
    }

    @Test
    fun `should throw exception when skipping beyond available bytes`() {
        assertFailsWith<IllegalArgumentException> {
            packet.skip(100)
        }
    }

    @Test
    fun `should return correct number of available bytes`() {
        // Arrange
        val testBytes = ByteArray(10)
        byteBuf.writeBytes(testBytes)

        // Act & Assert
        assertEquals(10, packet.available())
        
        packet.skip(5)
        assertEquals(5, packet.available())
    }

    @Test
    fun `should seek to specified position`() {
        // Arrange
        byteBuf.writeBytes(ByteArray(10))

        // Act
        packet.seek(5)

        // Assert
        assertEquals(5, byteBuf.readerIndex())
    }

    @Test
    fun `should throw exception when seeking to negative position`() {
        assertFailsWith<IllegalArgumentException> {
            packet.seek(-1)
        }
    }

    @Test
    fun `should return current reader position`() {
        // Arrange
        byteBuf.writeBytes(ByteArray(10))
        packet.skip(5)

        // Act & Assert
        assertEquals(5, packet.getPosition())
    }

    @Test
    fun `should return copy of buffer contents`() {
        // Arrange
        val testBytes = byteArrayOf(1, 2, 3, 4, 5)
        byteBuf.writeBytes(testBytes)

        // Act
        val result = packet.getBytes()

        // Assert
        assertContentEquals(testBytes, result)
    }

    @Suppress("RemoveExplicitTypeArguments") // or assertNotEquals will be a syntax error
    @Test
    fun `should be equal when containing same buffer`() {
        // Arrange
        val otherByteBuf = Unpooled.buffer().writeByte(42)
        val otherPacket = ByteBufInPacket(otherByteBuf)

        // Act & Assert
        assertNotEquals<ByteBufInPacket?>(packet, null)
        assertNotEquals<Any>(packet, "not a packet")
        assertEquals(packet, packet)
        assertNotEquals(packet, otherPacket)
        assertEquals(packet.hashCode(), packet.hashCode())
    }

    @Test
    fun `should provide string representation with hex dump`() {
        // Arrange
        val testBytes = byteArrayOf(1, 2, 3, 4, 5)
        byteBuf.writeBytes(testBytes)
        packet.skip(2)

        // Act
        val result = packet.toString()

        // Assert
        assertTrue(result.startsWith("ByteBufInPacket["))
        assertTrue(result.endsWith("]"))
    }
}

