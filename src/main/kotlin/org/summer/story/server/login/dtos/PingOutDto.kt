package org.summer.story.server.login.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.login.LoginSendOpcode

class PingOutDto : OutDto() {
    override fun toString(): String {
        return "[Ping]"
    }

    override fun writePacket(packet: ByteBufOutPacket) {
        /*
         * Creates a ping packet with the following structure:
         *
         * | Offset | Length | Description         |
         * |--------|--------|---------------------|
         * | 0x00   | 2      | Header (0x11)       |
         * Total Length: 2 bytes
         */

        packet.writeShort(LoginSendOpcode.PING.value)
    }
}

