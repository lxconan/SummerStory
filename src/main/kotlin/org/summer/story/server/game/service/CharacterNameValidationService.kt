package org.summer.story.server.game.service

import org.summer.story.data.CharacterRepository
import java.util.regex.Pattern

class CharacterNameValidationService(
    private val characterRepository: CharacterRepository
) {
    companion object {
        private val BLOCKED_NAME_PART = listOf(
            "admin", "owner", "moderator", "intern", "donor", "administrator", "fredrick", "help", "helper", "alert",
            "notice", "maplestory", "fuck", "wizet", "fucking", "negro", "fuk", "fuc", "penis", "pussy", "asshole",
            "gay", "nigger", "homo", "suck", "cum", "shit", "shitty", "condom", "security", "official", "rape", "nigga",
            "sex", "tit", "boner", "orgy", "clit", "asshole", "fatass", "bitch", "support", "gamemaster", "cock", "gaay",
            "gm", "operate", "master", "sysop", "party", "gamemaster", "community", "message", "event", "test", "meso",
            "scania", "yata", "asiasoft", "henesys"
        )

        private val PRECOMPILED_PATTERN = Pattern.compile("^[a-zA-Z0-9]{3,12}$")
    }

    fun canCreateCharacter(name: String): Boolean {
        val lowerCaseName = name.lowercase()
        if (BLOCKED_NAME_PART.any { lowerCaseName.contains(it) }) {
            return false
        }

        if (PRECOMPILED_PATTERN.matcher(name).matches().not()) {
            return false
        }

        return characterRepository.existByName(name).not()
    }
}