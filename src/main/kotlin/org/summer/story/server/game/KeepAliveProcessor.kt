package org.summer.story.server.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.players.Player

class KeepAliveProcessor : GameProcessor {
    override fun process(player: Player, msg: InPacket) {
        player.pongReceived()
    }
}
