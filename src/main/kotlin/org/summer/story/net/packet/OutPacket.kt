package org.summer.story.net.packet

import java.awt.Point
import java.nio.charset.Charset

/**
 * Interface for writing data to a packet in little-endian byte order.
 */
interface OutPacket : Packet {
    /**
     * Writes a byte value to the packet.
     * @param value The byte value to write
     */
    fun writeByte(value: Byte)

    /**
     * Writes the least significant 8 bits of an integer as a byte to the packet.
     * @param value The integer value to write (only lowest 8 bits are used)
     */
    fun writeByte(value: Int)

    /**
     * Writes a byte array to the packet.
     * @param value The byte array to write
     */
    fun writeBytes(value: ByteArray)

    /**
     * Writes a short value (16 bits) to the packet in little-endian order.
     * @param value The integer value to write as a short (only lowest 16 bits are used)
     */
    fun writeShort(value: Int)

    /**
     * Writes an integer value (32 bits) to the packet in little-endian order.
     * @param value The integer value to write
     */
    fun writeInt(value: Int)

    /**
     * Writes a long value (64 bits) to the packet in little-endian order.
     * @param value The long value to write
     */
    fun writeLong(value: Long)

    /**
     * Writes a boolean value as a single byte to the packet (1 for true, 0 for false).
     * @param value The boolean value to write
     */
    fun writeBool(value: Boolean)

    /**
     * Writes a string to the packet with its length as a short prefix.
     * The string is encoded using the specified charset.
     * @param value The string to write (must be less than Short.MAX_VALUE bytes when encoded)
     * @param charset The charset to use for string encoding
     * @throws IllegalArgumentException if the encoded string is longer than Short.MAX_VALUE bytes
     */
    fun writeString(value: String, charset: Charset)

    /**
     * Writes a fixed-length string to the packet without length prefix.
     * The string is encoded using the specified charset.
     * @param value The string to write (must be less than Short.MAX_VALUE bytes when encoded)
     * @param charset The charset to use for string encoding
     * @throws IllegalArgumentException if the encoded string is longer than Short.MAX_VALUE bytes
     */
    fun writeFixedString(value: String, charset: Charset)

    /**
     * Writes a Point as two short values (x, y) to the packet in little-endian order.
     * @param value The Point containing x and y coordinates
     */
    fun writePos(value: Point)

    /**
     * Writes the specified number of zero bytes to the packet.
     * @param numberOfBytes The number of zero bytes to write
     */
    fun skip(numberOfBytes: Int)
}