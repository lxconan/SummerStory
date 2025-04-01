package org.summer.story.server.dtos

import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.SendOpcode

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