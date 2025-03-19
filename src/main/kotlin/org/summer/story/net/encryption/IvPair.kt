package org.summer.story.net.encryption

class IvPair(private val enableDebugMode: Boolean = false) {
    val send: ByteArray = generateSend()
    val receive: ByteArray = generateReceive()

    private fun generateSend(): ByteArray {
        return byteArrayOf(82, 48, 120, getRandomByte())
    }

    private fun generateReceive(): ByteArray {
        return byteArrayOf(70, 114, 122, getRandomByte())
    }

    private fun getRandomByte(): Byte {
        return if (enableDebugMode) 0x23 else (Math.random() * 255).toInt().toByte()
    }
}