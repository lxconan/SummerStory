package org.summer.story.server.login.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.login.LoginReceiveOpcode
import org.summer.story.server.players.Player

class KeepAliveProcessor : LoginServerGameProcessor {
    override fun getOpcode(): LoginReceiveOpcode = LoginReceiveOpcode.PONG

    override fun process(player: Player, msg: InPacket) {
        player.pongReceived()
    }
}
