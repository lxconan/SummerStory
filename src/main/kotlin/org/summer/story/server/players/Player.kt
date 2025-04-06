package org.summer.story.server.players

import io.netty.channel.Channel
import org.slf4j.LoggerFactory
import org.summer.story.server.NetworkContext
import org.summer.story.server.SendPacketService
import org.summer.story.server.TimeService
import org.summer.story.server.dtos.OutDto
import java.util.concurrent.atomic.AtomicBoolean

// Please note that the player can be accessed from netty's event loop thread and from player's thread. So all the
// methods in the implementation should be thread-safe.
interface Player {
    val clientIp: String
    var accountContext: AccountContext?

    fun updateChannel(channel: Channel)
    fun isPongReceivedAfter(pingedAt: Long): Boolean
    fun pongReceived()
    fun isClosed(): Boolean
    fun close()
    fun respond(dto: OutDto)
}

class PlayerImpl(
    private val timeService: TimeService,
    private val sendPacketService: SendPacketService,
    networkContext: NetworkContext
) : Player {
    companion object {
        private val logger = LoggerFactory.getLogger(Player::class.java)
    }

    private var _ioChannel: Channel? = null
    private var _lastPongAt: Long = 0

    override val clientIp: String
    override var accountContext: AccountContext? = null

    private val _isClosed: AtomicBoolean = AtomicBoolean(false)

    init {
        require(networkContext.isValid()) { "Network context is invalid" }
        clientIp = networkContext.clientIp!!
    }

    override fun updateChannel(channel: Channel) {
        if (_isClosed.get()) {
            throw IllegalStateException("Player is closed, cannot update channel")
        }

        synchronized(this) {
            _ioChannel = channel
        }
    }

    override fun isPongReceivedAfter(pingedAt: Long): Boolean = synchronized(this) { _lastPongAt >= pingedAt }

    override fun pongReceived() = synchronized(this) {
        _lastPongAt = timeService.currentTimeMillis()
    }

    override fun isClosed(): Boolean = safelyGetChannel() == null

    override fun close() {
        // The Channel.close() method in Netty is thread-safe. Netty ensures that the close() method can be safely
        // called from any thread, and it will handle the necessary synchronization internally. This means you do
        // not need to synchronize the call to close() yourself.
        if (!_isClosed.compareAndSet(false, true)) {
            logger.warn("Player is already closed, cannot close again.")
            return
        }
        synchronized(this) { _ioChannel }?.close()
    }

    override fun respond(dto: OutDto) {
        if (_isClosed.get()) {
            logger.warn("Player is closed, cannot send packet: {}", dto.toString())
            return
        }

        val theChannel: Channel? = safelyGetChannel()
        if (theChannel == null) {
            logger.warn("Channel is null or inactive, cannot send packet: {}", dto.toString())
            return
        }

        // The channel.writeAndFlush() method in Netty is thread-safe. It can be safely called from any thread without
        // additional synchronization.
        sendPacketService.sendPacket(theChannel, dto)
    }

    private fun safelyGetChannel(): Channel? {
        if (_isClosed.get()) { return null }

        val theChannel: Channel? = synchronized(this) { _ioChannel }

        // The Channel.isActive method in Netty is thread-safe. It can be safely called from any thread without
        // additional synchronization.
        if (theChannel == null || !theChannel.isActive) {
            return null
        }

        return theChannel
    }
}