package org.summer.story.server.worlds

import java.util.concurrent.atomic.AtomicInteger

class GameChannelImpl(
    override val world: World,
    override val id: Int,
    override val maxPlayers: Int = 100,
) : GameChannel {
    private var onlinePlayersAtomic: AtomicInteger = AtomicInteger(0)
    override val playerCapacity: Int
        get() = maxPlayers - onlinePlayersAtomic.get()
    override val onlinePlayers: Int
        get() = onlinePlayersAtomic.get()

    fun increasePlayerOrFail(): Boolean {
        // this is just a temporary solution. May be change later.
        val currentPlayerCount = onlinePlayersAtomic.get()
        if (currentPlayerCount >= maxPlayers) {
            return false
        }

        return onlinePlayersAtomic.compareAndSet(currentPlayerCount, currentPlayerCount + 1)
    }
}