package org.summer.story.server.worlds

class WorldImpl(
    override val id: Int,
    override val name: String,
    override val eventMessage: String = "Welcome to SummerStory!"
) : World {
    override val channels: List<GameChannel> = listOf(GameChannelImpl(this, 1, 100))
    override val flag : Int = 0
    override val maxPlayers: Int
        get() = channels.sumOf { it.maxPlayers }
    override val onlinePlayers: Int
        get() = channels.sumOf { it.onlinePlayers }
}
