package org.summer.story.server.login

enum class LoginSendOpcode(val value: Int = -2) {
    LOGIN_STATUS(0x00),
    SERVER_STATUS(0x03),
    VIEW_ALL_CHARACTERS(0x08),
    SERVER_LIST(0x0A),
    CHARACTER_LIST(0x0B),
    CHARACTER_NAME_RESPONSE(0x0D),
    PING(0x11),
    LAST_CONNECTED_WORLD(0x1A),
    RECOMMENDED_WORLD_MESSAGE(0x1B),
}

