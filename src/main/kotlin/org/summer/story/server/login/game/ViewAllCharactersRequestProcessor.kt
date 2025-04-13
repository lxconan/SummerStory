package org.summer.story.server.login.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.login.LoginReceiveOpcode
import org.summer.story.server.players.Player
import org.summer.story.server.players.sendCannotFindAnyCharacters

class ViewAllCharactersRequestProcessor : LoginServerGameProcessor {
    override fun getOpcode(): LoginReceiveOpcode = LoginReceiveOpcode.VIEW_ALL_CHARACTERS_REQUEST

    override fun process(player: Player, msg: InPacket) {
        // For now, we don't support view characters request. We will implement it later.
        player.sendCannotFindAnyCharacters()
    }
}