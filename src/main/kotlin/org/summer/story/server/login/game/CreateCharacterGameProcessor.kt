package org.summer.story.server.login.game

import org.slf4j.LoggerFactory
import org.summer.story.config.*
import org.summer.story.data.CharacterRepository
import org.summer.story.net.packet.InPacket
import org.summer.story.server.login.LoginReceiveOpcode
import org.summer.story.server.login.game.service.CharacterNameValidationService
import org.summer.story.server.players.Player
import java.nio.charset.Charset

class CreateCharacterGameProcessor(
    private val configuration: GlobalConfiguration,
    private val metadata: MapleMetadata,
    private val characterNameValidationService: CharacterNameValidationService
) : LoginServerGameProcessor {
    companion object {
        private val logger = LoggerFactory.getLogger(CreateCharacterGameProcessor::class.java)
    }
    
    override fun getOpcode(): LoginReceiveOpcode = LoginReceiveOpcode.CREATE_CHARACTER

    override fun process(player: Player, msg: InPacket) {
        val charset = configuration.packet.charsetObject
        val createCharacterDto = CreateCharacterInDto(msg, charset, metadata, characterNameValidationService)
    }
}

@Suppress("JoinDeclarationAndAssignment")
class CreateCharacterInDto(
    msg: InPacket,
    charset: Charset,
    metadata: MapleMetadata,
    characterNameValidationService: CharacterNameValidationService
) {
    val name: String
    val job: Int
    val face: Int
    val hair: Int
    val hairColor: Int
    val skinColor: Int
    val top: Int
    val bottom: Int
    val shoes: Int
    val weapon: Int
    val gender: Byte

    init {
        name = msg.readString(charset)
        if (characterNameValidationService.canCreateCharacter(name).not()) {
            throw IllegalArgumentException("Invalid character name: $name")
        }

        job = MapleJobCategory.fromCode(msg.readInt()).code
        face = metadata.faces.get(msg.readInt()).code
        hair = metadata.hairs.get(msg.readInt()).code
        hairColor = MapleHairColor.fromCode(msg.readInt()).code
        skinColor = MapleSkinColor.fromCode(msg.readInt()).code
        top = metadata.coats.get(msg.readInt()).code
        bottom = metadata.pants.get(msg.readInt()).code
        shoes = metadata.shoes.get(msg.readInt()).code
        weapon = metadata.weapons.get(msg.readInt()).code
        gender = MapleGender.fromCode(msg.readByte()).code
    }
}
