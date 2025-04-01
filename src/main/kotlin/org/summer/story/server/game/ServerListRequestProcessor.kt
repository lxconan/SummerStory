package org.summer.story.server.game

import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.*
import org.summer.story.server.worlds.WorldManager

class ServerListRequestProcessor(
    private val configuration: GlobalConfiguration,
    private val worldManager: WorldManager
) : GameProcessor {
    override fun getOpcode(): ReceiveOpcode {
        return ReceiveOpcode.SERVER_LIST_REQUEST
    }

    override fun process(player: Player, msg: InPacket) {
        player.sendWorldInformation(worldManager.world, configuration)
        player.sendWorldInformationComplete()
        player.selectLastConnectedWorld()
        player.selectRecommendedWorld()
    }
}

