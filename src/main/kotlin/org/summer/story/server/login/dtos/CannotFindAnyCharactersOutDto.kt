package org.summer.story.server.login.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.login.LoginSendOpcode

class CannotFindAnyCharactersOutDto: OutDto() {
    companion object {
        const val CANNOT_FIND_ANY_CHARACTER: Byte = 5
    }

    override fun toString(): String {
        return "[Cannot find any characters]"
    }

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(LoginSendOpcode.VIEW_ALL_CHARACTERS.value)
        packet.writeByte(CANNOT_FIND_ANY_CHARACTER)
        packet.writeByte(0) // Total worlds
        packet.writeByte(0) // Total characters
    }
}