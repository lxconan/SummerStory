package org.summer.story.server.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.Player

class LoginPasswordProcessor : GameProcessor {
    override fun getOpcode(): ReceiveOpcode = ReceiveOpcode.LOGIN_PASSWORD

    override fun process(player: Player, msg: InPacket) {
    }
}