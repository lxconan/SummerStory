package org.summer.story.server.login.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.login.LoginSendOpcode

class LastConnectedWorldOutDto: OutDto() {
    override fun toString(): String = "[Select World]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(LoginSendOpcode.LAST_CONNECTED_WORLD.value)
        packet.writeInt(0)
    }
}