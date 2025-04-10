package org.summer.story.server.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.Player

interface GameProcessor {
    fun getOpcode(): ReceiveOpcode
    fun process(player: Player, msg: InPacket)
}

interface LoginServerGameProcessor : GameProcessor