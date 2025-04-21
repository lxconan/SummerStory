package org.summer.story.server.worlds

interface GameChannel {
    val id: Int
    val maxPlayers: Int
    val playerCapacity: Int
    val onlinePlayers: Int
    val world: World
}