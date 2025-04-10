package org.summer.story.server.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.Player

class KeepAliveProcessor : LoginServerGameProcessor {
    override fun getOpcode(): ReceiveOpcode = ReceiveOpcode.PONG

    override fun process(player: Player, msg: InPacket) {
        player.pongReceived()
    }
}
