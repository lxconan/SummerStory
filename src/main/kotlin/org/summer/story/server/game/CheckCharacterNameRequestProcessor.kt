package org.summer.story.server.game

import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.game.service.CharacterNameValidationService
import org.summer.story.server.players.Player
import org.summer.story.server.players.sendCanCreateCharacter
import java.nio.charset.Charset

class CheckCharacterNameRequestProcessor(
    private val configuration: GlobalConfiguration,
    private val characterNameValidationService: CharacterNameValidationService
) : GameProcessor {
    override fun getOpcode(): ReceiveOpcode = ReceiveOpcode.CHECK_CHARACTER_NAME

    override fun process(player: Player, msg: InPacket) {
        val charset = configuration.packet.charsetObject

        val request = CheckCharacterNameRequestInDto(msg, charset)
        val canCreateCharacter: Boolean = characterNameValidationService.canCreateCharacter(request.name)
        player.sendCanCreateCharacter(request.name, !canCreateCharacter, charset)
    }
}

class CheckCharacterNameRequestInDto(msg: InPacket, charset: Charset) {
    val name: String

    init {
        name = msg.readString(charset)
    }
}