package org.summer.story.net.packet

class InvalidPacketException(message: String) : RuntimeException(message) {
    constructor() : this("Invalid packet")
    constructor(header: Int) : this("Invalid packet header: $header")
}