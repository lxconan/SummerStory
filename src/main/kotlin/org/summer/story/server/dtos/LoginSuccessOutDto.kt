package org.summer.story.server.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.SendOpcode
import java.nio.charset.Charset

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