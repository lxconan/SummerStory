package org.summer.story.server.game

import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.*
import org.summer.story.server.worlds.WorldDefinitions

class ServerListRequestProcessor(
    private val configuration: GlobalConfiguration
) : GameProcessor {
    override fun getOpcode(): ReceiveOpcode {
        return ReceiveOpcode.SERVER_LIST_REQUEST
    }

    override fun process(player: Player, msg: InPacket) {
        // We only support one world for now.
        val worldMetadata = WorldMetadata(
            worldName = WorldDefinitions.worldName,
            worldId = 1, // the world starts from 1
            worldFlag = 0, // god knows what this is
            eventMessage = "Welcome to SummerStory!"
        )

        // channel id starts from 1
        val channelsMetadata = (1..configuration.world.channelCount).map {
            GameChannelMetadata(it, WorldDefinitions.maxPlayerPerChannel)
        }

        player.sendWorldInformation(worldMetadata, channelsMetadata, configuration)
        player.sendWorldInformationComplete()
        player.selectLastConnectedWorld()
        player.selectRecommendedWorld()
    }
}

class GameChannelMetadata(
    val channelId: Int,
    val channelCapacity: Int
)

class WorldMetadata(
    val worldName: String,
    val worldId: Int,
    val worldFlag: Int,
    val eventMessage: String
)
