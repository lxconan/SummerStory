package org.summer.story.server.game

import org.slf4j.LoggerFactory
import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.Player
import org.summer.story.server.players.sendEmptyCharacterList
import org.summer.story.server.players.sendServerStatus
import org.summer.story.server.worlds.WorldManager

class CharacterListRequestProcessor(
    private val worldManager: WorldManager
) : GameProcessor {
    companion object {
        private val logger = LoggerFactory.getLogger(CharacterListRequestProcessor::class.java)
    }

    override fun getOpcode(): ReceiveOpcode {
        return ReceiveOpcode.CHARACTER_LIST_REQUEST
    }

    override fun process(player: Player, msg: InPacket) {
        val request = CharacterListRequestInDto(msg)
        if (request.worldId != 0.toByte() || request.channelId < 0) {
            logger.warn("Invalid worldId {} or channelId {}", request.worldId, request.channelId)
            player.sendServerStatus(WorldServerStatus.NOT_AVAILABLE)
            return
        }

        if (worldManager.world.channels.size <= request.channelId) {
            logger.warn("Invalid channelId {}", request.channelId)
            player.sendServerStatus(WorldServerStatus.NOT_AVAILABLE)
            return
        }

        if (worldManager.world.onlinePlayers >= worldManager.world.maxPlayers) {
            logger.warn("World {} is full", worldManager.world.id)
            player.sendServerStatus(WorldServerStatus.NOT_AVAILABLE)
            return
        }

        player.gameChannel = worldManager.world.channels[request.channelId.toInt()]
        player.sendEmptyCharacterList()
    }
}

class CharacterListRequestInDto(msg: InPacket) {
    val worldId: Byte
    val channelId: Byte

    init {
        msg.skip(1)
        worldId = msg.readByte()
        channelId = msg.readByte()
    }
}