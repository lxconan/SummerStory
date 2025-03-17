package org.summer.story.net.packet

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import java.awt.Point
import java.nio.charset.Charset
import kotlin.math.min

/**
 * Implementation of InPacket that reads data from a ByteBuf with little-endian encoding.
 * This class provides methods to read various data types from a Netty ByteBuf while maintaining
 * position tracking and providing debug information.
 *
 * Note: This class does not manage the lifecycle of the underlying ByteBuf.
 * The caller is responsible for properly releasing the ByteBuf when it's no longer needed.
 *
 * @property byteBuf The underlying ByteBuf containing the packet data
 */
class ByteBufInPacket(private val byteBuf: ByteBuf) : InPacket {
    companion object {
        private const val MAX_HEX_DUMP_LENGTH = 1024 // Maximum length for hex dump in toString
    }

    override fun readByte(): Byte = byteBuf.readByte()

    override fun readUnsignedByte(): Short = byteBuf.readUnsignedByte()

    override fun readShort(): Short = byteBuf.readShortLE()

    override fun readInt(): Int = byteBuf.readIntLE()

    override fun readLong(): Long = byteBuf.readLongLE()

    override fun readPos(): Point {
        val x = byteBuf.readShortLE()
        val y = byteBuf.readShortLE()
        return Point(x.toInt(), y.toInt())
    }

    override fun readString(charset: Charset): String {
        val length = readShort()
        require(length >= 0) { "String length cannot be negative: $length" }

        val bytes = ByteArray(length.toInt())
        byteBuf.readBytes(bytes)
        return String(bytes, charset)
    }

    override fun readBytes(numberOfBytes: Int): ByteArray {
        require(numberOfBytes >= 0) { "Number of bytes cannot be negative: $numberOfBytes" }
        require(numberOfBytes <= byteBuf.readableBytes()) { "Not enough readable bytes: requested $numberOfBytes, available ${byteBuf.readableBytes()}" }

        val bytes = ByteArray(numberOfBytes)
        byteBuf.readBytes(bytes)
        return bytes
    }

    override fun skip(numberOfBytes: Int) {
        require(numberOfBytes >= 0) { "Number of bytes cannot be negative: $numberOfBytes" }
        require(numberOfBytes <= byteBuf.readableBytes()) { "Cannot skip more bytes than available: $numberOfBytes > ${byteBuf.readableBytes()}" }
        
        byteBuf.skipBytes(numberOfBytes)
    }

    override fun available(): Int = byteBuf.readableBytes()

    override fun seek(byteOffset: Int) {
        require(byteOffset >= 0) { "Byte offset cannot be negative: $byteOffset" }
        require(byteOffset <= byteBuf.capacity()) { "Byte offset exceeds buffer capacity: $byteOffset > ${byteBuf.capacity()}" }
        
        byteBuf.readerIndex(byteOffset)
    }

    override fun getPosition(): Int = byteBuf.readerIndex()

    override fun getBytes(): ByteArray = ByteBufUtil.getBytes(byteBuf)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ByteBufInPacket) return false
        // This is really expensive, we have to check if we really need this.
        return byteBuf == other.byteBuf
    }

    override fun hashCode(): Int = byteBuf.hashCode()

    override fun toString(): String {
        val readerIndex = byteBuf.readerIndex()
        byteBuf.markReaderIndex()
        byteBuf.readerIndex(0)

        val hexDump = createLimitedHexDump(byteBuf, readerIndex)
        byteBuf.resetReaderIndex()
        return "ByteBufInPacket[$hexDump]"
    }

    private fun createLimitedHexDump(buf: ByteBuf, position: Int): String {
        val length = min(buf.readableBytes(), MAX_HEX_DUMP_LENGTH)
        val hexDump = StringBuilder(ByteBufUtil.hexDump(buf, 0, length))
        
        if (position * 2 < hexDump.length) {
            hexDump.insert(position * 2, '_')
        }
        
        if (length < buf.readableBytes()) {
            hexDump.append("...")
        }
        
        return hexDump.toString()
    }
}