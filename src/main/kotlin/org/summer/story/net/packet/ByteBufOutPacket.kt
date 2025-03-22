package org.summer.story.net.packet

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import java.awt.Point
import java.nio.charset.Charset

/**
 * Implementation of OutPacket that writes data to a ByteBuf with little-endian encoding.
 * This class provides methods to write various data types to a Netty ByteBuf.
 * 
 * Note: This class implements AutoCloseable to ensure proper cleanup of the underlying ByteBuf.
 * The caller should use try-with-resources or explicitly call close() when done with the packet.
 */
class ByteBufOutPacket : OutPacket, AutoCloseable {
    private val byteBuf: ByteBuf = Unpooled.buffer(64)

    override fun writeByte(value: Byte) {
        byteBuf.writeByte(value.toInt())
    }

    override fun writeByte(value: Int) {
        // Netty's implementation directly masks the int with 0xFF, so we don't need to do anything special.
        byteBuf.writeByte(value)
    }

    override fun writeBytes(value: ByteArray) {
        require(value.isNotEmpty()) { "Byte array cannot be empty" }
        byteBuf.writeBytes(value)
    }

    override fun writeShort(value: Int) {
        byteBuf.writeShortLE(value)
    }

    override fun writeInt(value: Int) {
        byteBuf.writeIntLE(value)
    }

    override fun writeLong(value: Long) {
        byteBuf.writeLongLE(value)
    }

    override fun writeBool(value: Boolean) {
        byteBuf.writeByte(if (value) 1 else 0)
    }

    override fun writeString(value: String, charset: Charset) {
        require(value.isNotEmpty()) { "String cannot be empty" }
        val bytes: ByteArray = value.toByteArray(charset)
        require(bytes.size <= Short.MAX_VALUE) { "String is too long to write: ${bytes.size}" }

        writeShort(bytes.size)
        writeBytes(bytes)
    }

    override fun writeFixedString(value: String, charset: Charset) {
        require(value.isNotEmpty()) { "String cannot be empty" }
        val bytes: ByteArray = value.toByteArray(charset)
        require(bytes.size <= Short.MAX_VALUE) { "String is too long to write: ${bytes.size}" }

        writeBytes(bytes)
    }

    override fun writePos(value: Point) {
        requireNotNull(value) { "Point cannot be null" }
        writeShort(value.x)
        writeShort(value.y)
    }

    override fun skip(numberOfBytes: Int) {
        require(numberOfBytes >= 0) { "Number of bytes cannot be negative: $numberOfBytes" }
        writeBytes(ByteArray(numberOfBytes))
    }

    override fun getBytes(): ByteArray {
        return ByteBufUtil.getBytes(byteBuf)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ByteBufOutPacket

        // This is really expensive, we have to check if we really need this.
        return byteBuf == other.byteBuf
    }

    override fun hashCode(): Int {
        return byteBuf.hashCode()
    }

    override fun close() {
        byteBuf.release()
    }
}

