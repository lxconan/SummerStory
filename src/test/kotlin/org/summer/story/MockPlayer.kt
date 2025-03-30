package org.summer.story

import io.netty.channel.Channel
import org.summer.story.server.dtos.OutDto
import org.summer.story.server.players.AccountContext
import org.summer.story.server.players.Player

class MockPlayer(private val player: Player) : Player {
    val responds: MutableList<OutDto> = mutableListOf()

    override val clientIp: String
        get() = player.clientIp
    override var accountContext: AccountContext?
        get() = player.accountContext
        set(value) { player.accountContext = value }

    override fun updateChannel(channel: Channel) = player.updateChannel(channel)

    override fun isPongReceivedAfter(pingedAt: Long): Boolean = player.isPongReceivedAfter(pingedAt)

    override fun pongReceived() = player.pongReceived()

    override fun isClosed(): Boolean = player.isClosed()

    override fun close() = player.close()

    override fun respond(dto: OutDto) {
        responds.add(dto)
    }
}