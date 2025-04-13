package org.summer.story.config

import kotlinx.serialization.Serializable

class MapleMetadata {
    val faces: MapleCodeAndNameMetadata = MapleCodeAndNameMetadata("Faces", "/maple_faces.yaml")
    val hairs: MapleCodeAndNameMetadata = MapleCodeAndNameMetadata("Hairs", "/maple_hairs.yaml")
    val coats: MapleCodeAndNameMetadata = MapleCodeAndNameMetadata("Coats", "/maple_coats.yaml")
    val pants: MapleCodeAndNameMetadata = MapleCodeAndNameMetadata("Pants", "/maple_pants.yaml")
    val shoes: MapleCodeAndNameMetadata = MapleCodeAndNameMetadata("Shoes", "/maple_shoes.yaml")
    val weapons: MapleCodeAndNameMetadata = MapleCodeAndNameMetadata("Weapons", "/maple_weapons.yaml")
}

class MapleCodeAndNameMetadata(private val type: String, path: String) {
    private val index: Map<Int, MapleCodeAndNameItem>

    init {
        val collection: List<MapleCodeAndNameItem> = loadEmbeddedResource(path) { listOf() }
        index = collection.associateBy { it.code }
    }

    fun get(code: Int): MapleCodeAndNameItem {
        return index[code] ?: throw IllegalArgumentException("No $type found for code: $code")
    }
}

@Serializable
class MapleCodeAndNameItem(val code: Int, val name: String)

enum class MapleJobCategory(val code: Int) {
    CYGNUS_KNIGHT(0),
    EXPLORER(1),
    ARAN(2);

    companion object {
        private val map = MapleJobCategory.entries.associateBy(MapleJobCategory::code)

        fun fromCode(code: Int): MapleJobCategory {
            return map[code]?: throw IllegalArgumentException("No MapleJob found for code: $code")
        }
    }
}

enum class MapleGender(val code: Byte) {
    MALE(0),
    FEMALE(1);

    companion object {
        private val map = MapleGender.entries.associateBy(MapleGender::code)

        fun fromCode(code: Byte): MapleGender {
            return map[code]?: throw IllegalArgumentException("No MapleGender found for code: $code")
        }
    }
}

enum class MapleSkinColor(val code: Int) {
    NORMAL(0),
    DARK(1),
    BLACK(2),
    PALE(3),
    BLUE(4),
    GREEN(5),
    WHITE(9),
    PINK(10);

    companion object {
        private val map = MapleSkinColor.entries.associateBy(MapleSkinColor::code)

        fun fromCode(code: Int): MapleSkinColor {
            return map[code]?: throw IllegalArgumentException("No MapleSkinColor found for code: $code")
        }
    }
}

enum class MapleHairColor(val code: Int) {
    BLACK(0),
    RED(1),
    ORANGE(2),
    BLONDE(3),
    GREEN(4),
    BLUE(5),
    PURPLE(6),
    BROWN(7),
    MISSING_NAME_BLACK(8),
    MISSING_NAME_RED(9);

    companion object {
        private val map = MapleHairColor.entries.associateBy(MapleHairColor::code)

        fun fromCode(code: Int): MapleHairColor {
            return map[code]?: throw IllegalArgumentException("No MapleHairColor found for code: $code")
        }
    }
}

@Suppress("unused")
enum class MapleJob(val code: Int) {
    BEGINNER(0),
    WARRIOR(100),
    FIGHTER(110),
    CRUSADER(111),
    HERO(112),
    PAGE(120),
    WHITE_KNIGHT(121),
    PALADIN(122),
    SPEARMAN(130),
    DRAGON_KNIGHT(131),
    DARK_KNIGHT(132),
    MAGICIAN(200),
    FIRE_POISON_WIZARD(210),
    FIRE_POISON_MAGE(211),
    FIRE_POISON_ARCHMAGE(212),
    ICE_LIGHTNING_WIZARD(220),
    ICE_LIGHTNING_MAGE(221),
    ICE_LIGHTNING_ARCHMAGE(222),
    CLERIC(230),
    PRIEST(231),
    BISHOP(232),
    BOWMAN(300),
    HUNTER(310),
    RANGER(311),
    BOWMASTER(312),
    CROSS_BOWMAN(320),
    SNIPER(321),
    MARKSMAN(322),
    THIEF(400),
    ASSASSIN(410),
    HERMIT(411),
    NIGHT_LORD(412),
    BANDIT(420),
    CHIEF_BANDIT(421),
    SHADOWER(422),
    PIRATE(500),
    BRAWLER(510),
    MARAUDER(511),
    BUCCANEER(512),
    GUNSLINGER(520),
    OUTLAW(521),
    CORSAIR(522),
    NOBLESSE(1000),
    DAWN_WARRIOR_1(1100),
    DAWN_WARRIOR_2(1110),
    DAWN_WARRIOR_3(1111),
    DAWN_WARRIOR_4(1112),
    BLAZE_WIZARD_1(1200),
    BLAZE_WIZARD_2(1210),
    BLAZE_WIZARD_3(1211),
    BLAZE_WIZARD_4(1212),
    WIND_ARCHER_1(1300),
    WIND_ARCHER_2(1310),
    WIND_ARCHER_3(1311),
    WIND_ARCHER_4(1312),
    NIGHT_WALKER_1(1400),
    NIGHT_WALKER_2(1410),
    NIGHT_WALKER_3(1411),
    NIGHT_WALKER_4(1412),
    THUNDER_BREAKER_1(1500),
    THUNDER_BREAKER_2(1510),
    THUNDER_BREAKER_3(1511),
    THUNDER_BREAKER_4(1512),
    LEGEND(2000),
    ARAN_1(2100),
    ARAN_2(2110),
    ARAN_3(2111),
    ARAN_4(2112),
    EVAN(2001),
    EVAN_1(2200),
    EVAN_2(2210),
    EVAN_3(2211),
    EVAN_4(2212),
    EVAN_5(2213),
    EVAN_6(2214),
    EVAN_7(2215),
    EVAN_8(2216),
    EVAN_9(2217),
    EVAN_10(2218),
    MANAGER(800),
    GM(900),
    SUPER_GM(910);

    companion object {
        private val map = entries.associateBy(MapleJob::code)

        fun fromCode(code: Int): MapleJob {
            return map[code]?: throw IllegalArgumentException("No MapleJob found for code: $code")
        }
    }
}