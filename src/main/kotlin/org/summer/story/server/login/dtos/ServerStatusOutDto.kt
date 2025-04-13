package org.summer.story.server.login.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.login.LoginSendOpcode
import org.summer.story.server.login.game.WorldServerStatus

class ServerStatusOutDto(
    val worldServerStatus: WorldServerStatus
) : OutDto() {
    override fun toString(): String = "[Server Status]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(LoginSendOpcode.SERVER_STATUS.value)
        packet.writeShort(worldServerStatus.value)
    }
}