package org.summer.story.server.worlds

import java.util.concurrent.atomic.AtomicInteger

class GameChannelImpl(
    private val world: World,
    override val id: Int,
    private val maxPlayers: Int = 100,
) : GameChannel {
    private var players: AtomicInteger = AtomicInteger(0)

    override val playerCapacity: Int
        get() = maxPlayers - players.get()
}