package org.summer.story.server.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.SendOpcode

class RecommendedWorldOutDto: OutDto() {
    override fun toString(): String = "[Recommended World]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(SendOpcode.RECOMMENDED_WORLD_MESSAGE.value)
        packet.writeByte(0) // recommended world size, no recommended world XD!
    }
}