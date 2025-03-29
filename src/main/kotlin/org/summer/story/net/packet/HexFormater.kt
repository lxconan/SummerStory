package org.summer.story.net.packet

@OptIn(ExperimentalStdlibApi::class)
object HexFormater {
    private val format: HexFormat = HexFormat {
        upperCase = true
        bytes { byteSeparator = " " }
    }

    private val compactFormat = HexFormat {
        upperCase = true
    }

    fun toCompactHexString(bytes: ByteArray): String {
        return bytes.toHexString(compactFormat)
    }

    fun toHexString(bytes: ByteArray): String {
        return bytes.toHexString(format)
    }

    fun toHexString(integer: Int): String {
        return integer.toHexString(format)
    }
}