package org.summer.story.server.login.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.login.LoginSendOpcode

class RecommendedWorldOutDto: OutDto() {
    override fun toString(): String = "[Recommended World]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(LoginSendOpcode.RECOMMENDED_WORLD_MESSAGE.value)
        packet.writeByte(0) // recommended world size, no recommended world XD!
    }
}