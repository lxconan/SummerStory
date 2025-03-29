package org.summer.story.net.encryption

import org.summer.story.net.packet.HexFormater

class HashAlgorithm {
    fun sha256(input: String): String {
        val bytes = input.toByteArray(charset = Charsets.UTF_8)
        val digest: ByteArray = java.security.MessageDigest.getInstance("SHA-256").digest(bytes)
        return HexFormater.toCompactHexString(digest)
    }
}