package org.summer.story.server.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.SendOpcode

class WorldInformationCompleteOutDto : OutDto() {
    override fun toString(): String = "[World Information Complete]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(SendOpcode.SERVER_LIST.value)
        packet.writeByte(0xFF)
    }
}