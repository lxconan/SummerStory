package org.summer.story.net.packet

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import java.awt.Point
import java.nio.charset.Charset

class ByteBufOutPacket : OutPacket {
    private val byteBuf: ByteBuf = Unpooled.buffer()

    override fun writeByte(value: Byte) {
        byteBuf.writeByte(value.toInt())
    }

    override fun writeByte(value: Int) {
        // Netty's implementation directly masks the int with 0xFF, so we don't need to do anything special.
        byteBuf.writeByte(value)
    }

    override fun writeBytes(value: ByteArray) {
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
        val bytes: ByteArray = value.toByteArray(charset)
        if (bytes.size > Short.MAX_VALUE) {
            throw IllegalArgumentException("String is too long to write: ${bytes.size}")
        }

        writeShort(bytes.size)
        writeBytes(bytes)
    }

    override fun writeFixedString(value: String, charset: Charset) {
        val bytes: ByteArray = value.toByteArray(charset)
        if (bytes.size > Short.MAX_VALUE) {
            throw IllegalArgumentException("String is too long to write: ${bytes.size}")
        }

        writeBytes(bytes)
    }

    override fun writePos(value: Point) {
        writeShort(value.x)
        writeShort(value.y)
    }

    override fun skip(numberOfBytes: Int) {
        writeBytes(ByteArray(numberOfBytes))
    }

    override fun getBytes(): ByteArray {
        return ByteBufUtil.getBytes(byteBuf)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ByteBufOutPacket
        return byteBuf == other.byteBuf
    }

    override fun hashCode(): Int {
        return byteBuf.hashCode()
    }
}

