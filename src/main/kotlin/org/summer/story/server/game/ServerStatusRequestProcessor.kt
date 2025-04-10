package org.summer.story.server.game

import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.Player
import org.summer.story.server.players.sendServerStatus
import org.summer.story.server.worlds.WorldManager

class ServerStatusRequestProcessor(private val worldManager: WorldManager) : LoginServerGameProcessor {
    override fun getOpcode(): ReceiveOpcode = ReceiveOpcode.SERVER_STATUS_REQUEST

    override fun process(player: Player, msg: InPacket) {
        val worldIndex = msg.readShort()
        if (worldIndex != 0.toShort()) {
            player.sendServerStatus(WorldServerStatus.NOT_AVAILABLE)
        } else {
            player.sendServerStatus(getWorldStatus())
        }
    }

    private fun getWorldStatus(): WorldServerStatus {
        val availableCapacity = worldManager.world.maxPlayers - worldManager.world.onlinePlayers
        return when {
            availableCapacity <= 0 -> WorldServerStatus.NOT_AVAILABLE
            availableCapacity <= worldManager.world.maxPlayers / 5 -> WorldServerStatus.FULL
            else -> WorldServerStatus.NORMAL
        }
    }
}

enum class WorldServerStatus(val value: Int) {
    NORMAL(0),
    FULL(1),
    NOT_AVAILABLE(2)
}
