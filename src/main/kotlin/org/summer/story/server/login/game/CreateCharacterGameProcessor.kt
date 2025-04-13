package org.summer.story.server.login.game

import org.slf4j.LoggerFactory
import org.summer.story.config.GlobalConfiguration
import org.summer.story.config.MapleJobCategory
import org.summer.story.config.MapleMetadata
import org.summer.story.net.packet.InPacket
import org.summer.story.server.login.LoginReceiveOpcode
import org.summer.story.server.players.Player
import java.nio.charset.Charset

class CreateCharacterGameProcessor(
    private val configuration: GlobalConfiguration,
    private val metadata: MapleMetadata
) : LoginServerGameProcessor {
    companion object {
        private val logger = LoggerFactory.getLogger(CreateCharacterGameProcessor::class.java)
    }
    
    override fun getOpcode(): LoginReceiveOpcode = LoginReceiveOpcode.CREATE_CHARACTER

    override fun process(player: Player, msg: InPacket) {
        val charset = configuration.packet.charsetObject
        val createCharacterDto = CreateCharacterInDto(msg, charset)
        logCreateCharacterDto(createCharacterDto)
    }

    private fun logCreateCharacterDto(createCharacterDto: CreateCharacterInDto) {
        logger.info(
            "CreateCharacterDto - Name: {}, Job: {}, Face: {}, Hair: {}, HairColor: {}, SkinColor: {}, Top: {}, Bottom: {}, Shoes: {}, Weapon: {}, Gender: {}",
            createCharacterDto.name,
            MapleJobCategory.fromCode(createCharacterDto.job),
            metadata.faces.get(createCharacterDto.face)?.name ?: "Unknown",
            metadata.hairs.get(createCharacterDto.hair)?.name ?: "Unknown",
            createCharacterDto.hairColor,
            createCharacterDto.skinColor,
            createCharacterDto.top,
            createCharacterDto.bottom,
            createCharacterDto.shoes,
            createCharacterDto.weapon,
            createCharacterDto.gender
        )
    }
}

class CreateCharacterInDto(msg: InPacket, charset: Charset) {
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
        job = msg.readInt()
        face = msg.readInt()
        hair = msg.readInt()
        hairColor = msg.readInt()
        skinColor = msg.readInt()
        top = msg.readInt()
        bottom = msg.readInt()
        shoes = msg.readInt()
        weapon = msg.readInt()
        gender = msg.readByte()
    }
}


