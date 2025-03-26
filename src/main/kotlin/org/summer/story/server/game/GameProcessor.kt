package org.summer.story.server.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.players.Player

interface GameProcessor {
    fun process(player: Player, msg: InPacket)
}