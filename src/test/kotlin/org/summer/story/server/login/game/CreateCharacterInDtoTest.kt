package org.summer.story.server.login.game

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.summer.story.DatabaseEnabledTest
import org.summer.story.InPacketFactory
import org.summer.story.config.*
import org.summer.story.server.login.game.service.CharacterNameValidationService
import kotlin.test.assertEquals

class CreateCharacterInDtoTest : DatabaseEnabledTest() {
    companion object {
        private val VALID_JOB: Int = MapleJobCategory.EXPLORER.code
        private const val VALID_FACE: Int = 20000 // Motivated Look (Black)
        private const val VALID_HAIR: Int = 30000 // Black Toben
        private val VALID_HAIR_COLOR: Int = MapleHairColor.BLACK.code
        private val VALID_SKIN_COLOR: Int = MapleSkinColor.NORMAL.code
        private const val VALID_TOP: Int = 1040001 // Black Blazer
        private const val VALID_BOTTOM: Int = 1060001 // Black Suit Pants
        private const val VALID_SHOES: Int = 1070001 // Black Santa Boots
        private const val VALID_WEAPON: Int = 1302000 // Sword
        private val VALID_GENDER: Byte = MapleGender.MALE.code
        private const val VALID_NAME: String = "goodname"

        @JvmStatic
        fun invalidCharacterInDtoTestData() = listOf(
            arrayOf("invalid name", VALID_JOB, VALID_FACE, VALID_HAIR, VALID_HAIR_COLOR, VALID_SKIN_COLOR, VALID_TOP,
                VALID_BOTTOM, VALID_SHOES, VALID_WEAPON, VALID_GENDER),
            arrayOf(VALID_NAME, -2, VALID_FACE, VALID_HAIR, VALID_HAIR_COLOR, VALID_SKIN_COLOR, VALID_TOP,
                VALID_BOTTOM, VALID_SHOES, VALID_WEAPON, VALID_GENDER),
            arrayOf(VALID_NAME, VALID_JOB, -2, VALID_HAIR, VALID_HAIR_COLOR, VALID_SKIN_COLOR, VALID_TOP,
                VALID_BOTTOM, VALID_SHOES, VALID_WEAPON, VALID_GENDER),
            arrayOf(VALID_NAME, VALID_JOB, VALID_FACE, -2, VALID_HAIR_COLOR, VALID_SKIN_COLOR, VALID_TOP,
                VALID_BOTTOM, VALID_SHOES, VALID_WEAPON, VALID_GENDER),
            arrayOf(VALID_NAME, VALID_JOB, VALID_FACE, VALID_HAIR, -2, VALID_SKIN_COLOR, VALID_TOP,
                VALID_BOTTOM, VALID_SHOES, VALID_WEAPON, VALID_GENDER),
            arrayOf(VALID_NAME, VALID_JOB, VALID_FACE, VALID_HAIR, VALID_HAIR_COLOR, -2, VALID_TOP,
                VALID_BOTTOM, VALID_SHOES, VALID_WEAPON, VALID_GENDER),
            arrayOf(VALID_NAME, VALID_JOB, VALID_FACE, VALID_HAIR, VALID_HAIR_COLOR, VALID_SKIN_COLOR, -2,
                VALID_BOTTOM, VALID_SHOES, VALID_WEAPON, VALID_GENDER),
            arrayOf(VALID_NAME, VALID_JOB, VALID_FACE, VALID_HAIR, VALID_HAIR_COLOR, VALID_SKIN_COLOR, VALID_TOP,
                -2, VALID_SHOES, VALID_WEAPON, VALID_GENDER),
            arrayOf(VALID_NAME, VALID_JOB, VALID_FACE, VALID_HAIR, VALID_HAIR_COLOR, VALID_SKIN_COLOR, VALID_TOP,
                VALID_BOTTOM, -2, VALID_WEAPON, VALID_GENDER),
            arrayOf(VALID_NAME, VALID_JOB, VALID_FACE, VALID_HAIR, VALID_HAIR_COLOR, VALID_SKIN_COLOR, VALID_TOP,
                VALID_BOTTOM, VALID_SHOES, -2, VALID_GENDER),
            arrayOf(VALID_NAME, VALID_JOB, VALID_FACE, VALID_HAIR, VALID_HAIR_COLOR, VALID_SKIN_COLOR, VALID_TOP,
                VALID_BOTTOM, VALID_SHOES, VALID_WEAPON, (-2).toByte()
            )
        )
    }

    @ParameterizedTest
    @MethodSource("invalidCharacterInDtoTestData")
    fun `should throw exception for invalid character name`(
        name: String, job: Int, face: Int, hair: Int, hairColor: Int, skinColor: Int,
        top: Int, bottom: Int, shoes: Int, weapon: Int, gender: Byte
    ) {
        // Given
        val charset = configuration.packet.charsetObject
        val metadata = koin.get<MapleMetadata>()
        val characterNameValidationService = koin.get<CharacterNameValidationService>()
        val msg = InPacketFactory.createCreateCharacterRequestWithoutOpCode(
            name, job, face, hair, hairColor, skinColor, top, bottom, shoes, weapon, gender, charset
        )

        // When/Then
        kotlin.test.assertFailsWith<IllegalArgumentException> {
            CreateCharacterInDto(msg, charset, metadata, characterNameValidationService)
        }
    }

    @Test
    fun `should create CreateCharacterInDto`() {
        // Given
        val charset = configuration.packet.charsetObject
        val metadata = koin.get<MapleMetadata>()
        val characterNameValidationService = koin.get<CharacterNameValidationService>()
        val msg = InPacketFactory.createCreateCharacterRequestWithoutOpCode(
            VALID_NAME, VALID_JOB, VALID_FACE, VALID_HAIR, VALID_HAIR_COLOR, VALID_SKIN_COLOR, VALID_TOP,
            VALID_BOTTOM, VALID_SHOES, VALID_WEAPON, VALID_GENDER, charset
        )

        // When
        val dto = CreateCharacterInDto(msg, charset, metadata, characterNameValidationService)

        // Then
        assertEquals(VALID_NAME, dto.name)
        assertEquals(VALID_JOB, dto.job)
        assertEquals(VALID_FACE, dto.face)
        assertEquals(VALID_HAIR, dto.hair)
        assertEquals(VALID_HAIR_COLOR, dto.hairColor)
        assertEquals(VALID_SKIN_COLOR, dto.skinColor)
        assertEquals(VALID_TOP, dto.top)
        assertEquals(VALID_BOTTOM, dto.bottom)
        assertEquals(VALID_SHOES, dto.shoes)
        assertEquals(VALID_WEAPON, dto.weapon)
        assertEquals(VALID_GENDER, dto.gender)
    }
}