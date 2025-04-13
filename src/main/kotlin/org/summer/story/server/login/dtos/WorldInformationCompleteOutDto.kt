package org.summer.story.server.login.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.login.LoginSendOpcode

class WorldInformationCompleteOutDto : OutDto() {
    override fun toString(): String = "[World Information Complete]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(LoginSendOpcode.SERVER_LIST.value)
        packet.writeByte(0xFF)
    }
}