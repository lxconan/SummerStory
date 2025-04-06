package org.summer.story.server.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.Player
import org.summer.story.server.players.sendCannotFindAnyCharacters

class ViewAllCharactersRequestProcessor : GameProcessor {
    override fun getOpcode(): ReceiveOpcode = ReceiveOpcode.VIEW_ALL_CHARACTERS_REQUEST

    override fun process(player: Player, msg: InPacket) {
        // For now, we don't support view characters request. We will implement it later.
        player.sendCannotFindAnyCharacters()
    }
}