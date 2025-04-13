package org.summer.story.server.login.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.login.LoginSendOpcode
import java.nio.charset.Charset

class CharacterNameResponseOutDto(val name: String, val forbidden: Boolean, val charset: Charset) : OutDto() {
    override fun toString(): String = "[CharacterNameResponse]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(LoginSendOpcode.CHARACTER_NAME_RESPONSE.value)
        packet.writeString(name, charset)
        packet.writeBool(forbidden)
    }
}