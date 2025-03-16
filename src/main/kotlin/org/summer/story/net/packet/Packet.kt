package org.summer.story.net.packet

/**
 * A packet is a data structure that can be written to and read from a byte buffer.
 */
interface Packet {

    /**
     * Gets the packet data as a byte array.
     * @return The packet data as a byte array
     */
    fun getBytes(): ByteArray
}

