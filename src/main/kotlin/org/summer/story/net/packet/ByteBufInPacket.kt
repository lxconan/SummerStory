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

    /**
     * Reads a single byte from the buffer.
     * @return The byte value read
     * @throws IndexOutOfBoundsException if there are not enough readable bytes
     */
    override fun readByte(): Byte = byteBuf.readByte()

    /**
     * Reads an unsigned byte from the buffer.
     * @return The unsigned byte value as a Short
     * @throws IndexOutOfBoundsException if there are not enough readable bytes
     */
    override fun readUnsignedByte(): Short = byteBuf.readUnsignedByte()

    /**
     * Reads a little-endian short from the buffer.
     * @return The short value read
     * @throws IndexOutOfBoundsException if there are not enough readable bytes
     */
    override fun readShort(): Short = byteBuf.readShortLE()

    /**
     * Reads a little-endian integer from the buffer.
     * @return The integer value read
     * @throws IndexOutOfBoundsException if there are not enough readable bytes
     */
    override fun readInt(): Int = byteBuf.readIntLE()

    /**
     * Reads a little-endian long from the buffer.
     * @return The long value read
     * @throws IndexOutOfBoundsException if there are not enough readable bytes
     */
    override fun readLong(): Long = byteBuf.readLongLE()

    /**
     * Reads a 2D position from the buffer as two little-endian shorts.
     * @return A Position object containing the x and y coordinates
     * @throws IndexOutOfBoundsException if there are not enough readable bytes
     */
    override fun readPos(): Point {
        val x = byteBuf.readShortLE()
        val y = byteBuf.readShortLE()
        return Point(x.toInt(), y.toInt())
    }

    /**
     * Reads a string from the buffer with the specified charset.
     * @param charset The charset to use for decoding the string
     * @return The decoded string
     * @throws IllegalArgumentException if the string length is negative or exceeds MAX_STRING_LENGTH
     * @throws IndexOutOfBoundsException if there are not enough readable bytes
     */
    override fun readString(charset: Charset): String {
        val length = readShort()
        require(length >= 0) { "String length cannot be negative: $length" }

        val bytes = ByteArray(length.toInt())
        byteBuf.readBytes(bytes)
        return String(bytes, charset)
    }

    /**
     * Reads the specified number of bytes from the buffer.
     * @param numberOfBytes The number of bytes to read
     * @return The byte array containing the read bytes
     * @throws IllegalArgumentException if numberOfBytes is negative
     * @throws IndexOutOfBoundsException if there are not enough readable bytes
     */
    override fun readBytes(numberOfBytes: Int): ByteArray {
        require(numberOfBytes >= 0) { "Number of bytes cannot be negative: $numberOfBytes" }
        require(numberOfBytes <= byteBuf.readableBytes()) { "Not enough readable bytes: requested $numberOfBytes, available ${byteBuf.readableBytes()}" }

        val bytes = ByteArray(numberOfBytes)
        byteBuf.readBytes(bytes)
        return bytes
    }

    /**
     * Skips the specified number of bytes in the buffer.
     * @param numberOfBytes The number of bytes to skip
     * @throws IllegalArgumentException if numberOfBytes is negative or exceeds available bytes
     */
    override fun skip(numberOfBytes: Int) {
        require(numberOfBytes >= 0) { "Number of bytes cannot be negative: $numberOfBytes" }
        require(numberOfBytes <= byteBuf.readableBytes()) { "Cannot skip more bytes than available: $numberOfBytes > ${byteBuf.readableBytes()}" }
        
        byteBuf.skipBytes(numberOfBytes)
    }

    /**
     * Returns the number of readable bytes remaining in the buffer.
     * @return The number of readable bytes
     */
    override fun available(): Int = byteBuf.readableBytes()

    /**
     * Sets the reader index to the specified position.
     * @param byteOffset The new reader index
     * @throws IllegalArgumentException if byteOffset is negative or exceeds the buffer capacity
     */
    override fun seek(byteOffset: Int) {
        require(byteOffset >= 0) { "Byte offset cannot be negative: $byteOffset" }
        require(byteOffset <= byteBuf.capacity()) { "Byte offset exceeds buffer capacity: $byteOffset > ${byteBuf.capacity()}" }
        
        byteBuf.readerIndex(byteOffset)
    }

    /**
     * Gets the current reader position in the buffer.
     * @return The current reader index
     */
    override fun getPosition(): Int = byteBuf.readerIndex()

    /**
     * Creates a copy of the buffer's contents.
     * @return A byte array containing the buffer's contents
     */
    override fun getBytes(): ByteArray = ByteBufUtil.getBytes(byteBuf)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ByteBufInPacket) return false
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

    /**
     * Creates a limited hex dump of the buffer with position marker.
     */
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