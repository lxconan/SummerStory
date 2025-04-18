package org.summer.story.server.login.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.login.LoginSendOpcode

class CharacterListOutDto(val availableCharacterSlot: Int) : OutDto() {
    companion object {
        const val NO_PIC: Byte = 2
    }

    override fun toString(): String {
        return "[CharacterList]"
    }

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(LoginSendOpcode.CHARACTER_LIST.value)
        packet.writeByte(0) // status
        packet.writeByte(0) // character size
        packet.writeByte(NO_PIC) // no pic system
        packet.writeInt(availableCharacterSlot) // available character slots
    }
}

