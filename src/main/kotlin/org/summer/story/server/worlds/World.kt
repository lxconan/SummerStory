package org.summer.story.server.worlds

interface World {
    val id: Int
    val name: String
    val eventMessage: String
    val channels: List<GameChannel>
    val flag: Int
}