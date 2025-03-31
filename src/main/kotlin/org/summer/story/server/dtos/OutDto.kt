package org.summer.story.server.dtos

import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.SendOpcode
import org.summer.story.server.game.GameChannelMetadata
import org.summer.story.server.game.WorldMetadata
import java.nio.charset.Charset

abstract class OutDto {
    abstract override fun toString(): String

    protected abstract fun writePacket(packet: ByteBufOutPacket)

    fun toPacket(): ByteBufOutPacket {
        val packet = ByteBufOutPacket()
        writePacket(packet)
        return packet
    }
}

class PingOutDto : OutDto() {
    override fun toString(): String {
        return "[Ping]"
    }

    override fun writePacket(packet: ByteBufOutPacket) {
        /*
         * Creates a ping packet with the following structure:
         *
         * | Offset | Length | Description         |
         * |--------|--------|---------------------|
         * | 0x00   | 2      | Header (0x11)       |
         * Total Length: 2 bytes
         */

        packet.writeShort(SendOpcode.PING.value)
    }
}

class LoginFailedOutDto(val reason: Byte) : OutDto() {
    enum class WellKnownLoginFailedReason(val reason: Byte) {
        PLAYER_DELETED_OR_BLOCKED(3),
        INCORRECT_PASSWORD(4),
        ACCOUNT_NOT_FOUND(5),
        TOO_MANY_LOGIN_ATTEMPTS(6),
        ALREADY_LOGGED_IN(7),
        UNKNOWN_REASON(9),
        WRONG_GATEWAY(14),
        STILL_PROCESSING_YOUR_REQUEST(15)
    }

    override fun toString(): String {
        return "[LoginFailed]"
    }

    override fun writePacket(packet: ByteBufOutPacket) {
        /*
         * Creates a login failed packet with the following structure:
         *
         * | Offset | Length | Description         |
         * |--------|--------|---------------------|
         * | 0x00   | 2      | Header (0x00)       |
         * | 0x02   | 1      | Reason              |
         * | 0x03   | 1      | Unknown             |
         * | 0x04   | 4      | Unknown             |
         * Total Length: 8 bytes
         */

        packet.writeShort(SendOpcode.LOGIN_STATUS.value)
        packet.writeByte(reason)
        packet.writeByte(0)
        packet.writeInt(0)
    }
}

class LoginSuccessOutDto(
    val accountId: Int,
    val accountName: String,
    private val charset: Charset
) : OutDto() {
    override fun toString(): String {
        return "[LoginSuccess]"
    }

    override fun writePacket(packet: ByteBufOutPacket) {
        /*
         * Creates a login success packet with the following structure:
         *
         * | Offset | Length | Description         |
         * |--------|--------|---------------------|
         * | 0x00   | 2      | Header (0x00)       |
         * | 0x02   | 4      | Unknown             |
         * | 0x06   | 2      | Unknown             |
         * | 0x08   | 4      | Account ID          |
         * | 0x0C   | 1      | Gender              |
         * | 0x0D   | 1      | Game Master Flag    |
         * | 0x0E   | 1      | Admin Byte          |
         * | 0x0F   | 1      | Country Code        |
         * | 0x10   | N      | Account Name        |
         * | 0x10+N | 1      | Unknown             |
         * | 0x11+N | 1      | Quiet Banned Flag   |
         * | 0x12+N | 8      | Banned Timestamp    |
         * | 0x1A+N | 8      | Creation Timestamp  |
         * | 0x22+N | 4      | World List Flag     |
         * | 0x26+N | 1      | Pin System Flag     |
         * | 0x27+N | 1      | Pic System Flag     |
         * Total Length: 40+N bytes
         */

        packet.writeShort(SendOpcode.LOGIN_STATUS.value)

        packet.writeInt(0)
        packet.writeShort(0)

        packet.writeInt(accountId)
        packet.writeByte(0) // gender

        packet.writeBool(false) // not game master
        packet.writeByte(0) // admin byte, 0x80 for admin
        packet.writeByte(0) // country code

        packet.writeString(accountName, charset)
        packet.writeByte(0)

        packet.writeByte(0) // not quiet banned
        packet.writeLong(0) // no banned timestamp
        packet.writeLong(0) // no creation timestamp

        packet.writeInt(1) // remove the select world list

        packet.writeByte(1) // disable pin-system
        packet.writeByte(2) // disable pic-system
    }
}

class WorldInformationOutDto(
    val worldMetadata: WorldMetadata,
    val gameChannelsMetadata: List<GameChannelMetadata>,
    val configuration: GlobalConfiguration
) : OutDto() {
    override fun toString(): String = "[World Information]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(SendOpcode.SERVER_LIST.value)
        packet.writeByte(worldMetadata.worldId)
        packet.writeString(worldMetadata.worldName, configuration.packet.charsetObject)
        packet.writeByte(worldMetadata.worldFlag)
        packet.writeString(worldMetadata.eventMessage, configuration.packet.charsetObject)
        packet.writeByte(100) // rate modifier, don't know why
        packet.writeByte(0) // event experience, don't know why
        packet.writeByte(100) // rate modifier, don't know why
        packet.writeByte(0) // event drop, don't know why
        packet.writeByte(0) // don't know what it is
        packet.writeByte(gameChannelsMetadata.size)
        for (gameChannelMetadata in gameChannelsMetadata) {
            packet.writeString(
                worldMetadata.worldName + "-" + gameChannelMetadata.channelId,
                configuration.packet.charsetObject
            )
            packet.writeInt(gameChannelMetadata.channelCapacity)

            packet.writeByte(1) // world id, not sure why
            packet.writeByte(gameChannelMetadata.channelId - 1) // channel id
            packet.writeBool(false) // adult channel
        }
        packet.writeShort(0) // packet end
    }
}

class WorldInformationCompleteOutDto : OutDto() {
    override fun toString(): String = "[World Information Complete]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(SendOpcode.SERVER_LIST.value)
        packet.writeByte(0xFF)
    }
}

class LastConnectedWorldOutDto: OutDto() {
    override fun toString(): String = "[Select World]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(SendOpcode.LAST_CONNECTED_WORLD.value)
        packet.writeInt(0)
    }
}

class RecommendedWorldOutDto: OutDto() {
    override fun toString(): String = "[Recommended World]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(SendOpcode.RECOMMENDED_WORLD_MESSAGE.value)
        packet.writeByte(0) // recommended world size, no recommended world XD!
    }
}