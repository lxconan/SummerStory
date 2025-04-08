package org.summer.story.server

enum class ReceiveOpcode(val value: Int = -2) {
    LOGIN_PASSWORD(0x01),
    CHARACTER_LIST_REQUEST(0x05),
    SERVER_STATUS_REQUEST(0x06),
    SERVER_LIST_REQUEST(0x0B),
    VIEW_ALL_CHARACTERS_REQUEST(0x0D),
    PONG(0x18),
}