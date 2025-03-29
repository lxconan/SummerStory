package org.summer.story.server.game

import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.Player
import org.summer.story.server.players.declareLoginFailedForIncorrectPassword
import java.nio.charset.Charset

class LoginPasswordProcessor(private val configuration: GlobalConfiguration) : GameProcessor {
    override fun getOpcode(): ReceiveOpcode = ReceiveOpcode.LOGIN_PASSWORD

    override fun process(player: Player, msg: InPacket) {
        val loginRequest = LoginPasswordInDto(msg, configuration.packet.charsetObject)
        player.declareLoginFailedForIncorrectPassword()
    }
}

@Suppress("JoinDeclarationAndAssignment")
class LoginPasswordInDto(msg: InPacket, charset: Charset) {
    val accountName: String
    val password: String
    val hardwareId: ByteArray

    init {
        accountName = msg.readString(charset)
        password = msg.readString(charset)
        msg.skip(6) // network address masks
        hardwareId = msg.readBytes(4)
    }
}