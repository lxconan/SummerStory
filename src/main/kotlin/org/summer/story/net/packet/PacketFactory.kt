package org.summer.story.net.packet

object PacketFactory {
    fun createHello(mapleVersion: Int, sendIv: ByteArray, receiveIv: ByteArray): Packet {
        val p = ByteBufOutPacket()
        p.writeShort(0x0E)
        p.writeShort(mapleVersion)
        p.writeShort(1)
        p.writeByte(49)
        p.writeBytes(receiveIv)
        p.writeBytes(sendIv)
        p.writeByte(8)
        return p
    }
}