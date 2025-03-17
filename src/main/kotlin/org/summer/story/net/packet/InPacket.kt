package org.summer.story.net.packet

import java.awt.Point
import java.nio.charset.Charset

interface InPacket : Packet {
    fun readByte(): Byte
    fun readUnsignedByte(): Short
    fun readShort(): Short
    fun readInt(): Int
    fun readLong(): Long
    fun readPos(): Point
    fun readString(charset: Charset): String
    fun readBytes(numberOfBytes: Int): ByteArray
    fun skip(numberOfBytes: Int)
    fun available(): Int
    fun seek(byteOffset: Int)
    fun getPosition(): Int
}
