package org.summer.story.net.packet

import java.awt.Point
import java.nio.charset.Charset

interface OutPacket : Packet {
    fun writeByte(value: Byte)
    fun writeByte(value: Int)
    fun writeBytes(value: ByteArray)
    fun writeShort(value: Int)
    fun writeInt(value: Int)
    fun writeLong(value: Long)
    fun writeBool(value: Boolean)
    fun writeString(value: String, charset: Charset)
    fun writeFixedString(value: String, charset: Charset)
    fun writePos(value: Point)
    fun skip(numberOfBytes: Int)
}