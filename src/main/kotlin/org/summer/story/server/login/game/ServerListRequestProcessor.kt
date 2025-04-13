package org.summer.story.server.login.game

import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.packet.InPacket
import org.summer.story.server.login.LoginReceiveOpcode
import org.summer.story.server.players.*
import org.summer.story.server.worlds.WorldManager

class ServerListRequestProcessor(
    private val configuration: GlobalConfiguration,
    private val worldManager: WorldManager
) : LoginServerGameProcessor {
    override fun getOpcode(): LoginReceiveOpcode {
        return LoginReceiveOpcode.SERVER_LIST_REQUEST
    }

    override fun process(player: Player, msg: InPacket) {
        player.sendWorldInformation(worldManager.world, configuration)
        player.sendWorldInformationComplete()
        player.selectLastConnectedWorld()
        player.selectRecommendedWorld()
    }
}

