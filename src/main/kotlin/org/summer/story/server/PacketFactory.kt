package org.summer.story.server

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.net.packet.Packet

object PacketFactory {
    fun createHello(mapleVersion: Int, sendIv: ByteArray, receiveIv: ByteArray): Packet {
        /*
         * Creates a handshake packet with the following structure:
         *
         * | Offset | Length | Description     |
         * |--------|--------|-----------------|
         * | 0x00   | 2      | Header (0x0E)   |
         * | 0x02   | 2      | Maple Version   |
         * | 0x04   | 2      | Sub Version (1) |
         * | 0x06   | 1      | Local Test (49) |
         * | 0x07   | 4      | Receive IV      |
         * | 0x0B   | 4      | Send IV         |
         * | 0x0F   | 1      | Unknown (8)     |
         * Total Length: 16 bytes
         */
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

    fun createPing(): Packet {
        /*
         * Creates a ping packet with the following structure:
         *
         * | Offset | Length | Description         |
         * |--------|--------|---------------------|
         * | 0x00   | 2      | Header (0x11)       |
         * Total Length: 2 bytes
         */

        val packet = ByteBufOutPacket()
        packet.writeShort(SendOpcode.PING.value)
        return packet
    }
}