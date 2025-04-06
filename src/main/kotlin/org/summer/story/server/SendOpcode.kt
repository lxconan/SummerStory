package org.summer.story.server

enum class SendOpcode(val value: Int = -2) {
    LOGIN_STATUS(0x00),
    SERVER_STATUS(0x03),
    VIEW_ALL_CHARACTERS(0x08),
    SERVER_LIST(0x0A),
    PING(0x11),
    LAST_CONNECTED_WORLD(0x1A),
    RECOMMENDED_WORLD_MESSAGE(0x1B),

}

