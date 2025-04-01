package org.summer.story.server.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.SendOpcode

class LastConnectedWorldOutDto: OutDto() {
    override fun toString(): String = "[Select World]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(SendOpcode.LAST_CONNECTED_WORLD.value)
        packet.writeInt(0)
    }
}