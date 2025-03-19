package org.summer.story.net.encryption

class IvPair {
    val send: ByteArray = generateSend()
    val receive: ByteArray = generateReceive()

    private fun generateSend(): ByteArray {
        return byteArrayOf(82, 48, 120, getRandomByte())
    }

    private fun generateReceive(): ByteArray {
        return byteArrayOf(70, 114, 122, getRandomByte())
    }

    private fun getRandomByte(): Byte {
        // return (Math.random() * 255).toInt().toByte()
        return 0x23
    }
}