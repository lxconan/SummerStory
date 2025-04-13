package org.summer.story.server.login.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.login.LoginReceiveOpcode
import org.summer.story.server.players.Player

interface GameProcessor {
    fun getOpcode(): LoginReceiveOpcode
    fun process(player: Player, msg: InPacket)
}

interface LoginServerGameProcessor : GameProcessor