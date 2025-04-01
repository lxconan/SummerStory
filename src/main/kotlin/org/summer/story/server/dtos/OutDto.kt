package org.summer.story.server.dtos

import org.summer.story.net.packet.ByteBufOutPacket

abstract class OutDto {
    abstract override fun toString(): String

    protected abstract fun writePacket(packet: ByteBufOutPacket)

    fun toPacket(): ByteBufOutPacket {
        val packet = ByteBufOutPacket()
        writePacket(packet)
        return packet
    }
}

