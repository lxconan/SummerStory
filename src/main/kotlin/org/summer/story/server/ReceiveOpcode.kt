package org.summer.story.server

enum class ReceiveOpcode(val value: Int = -2) {
    LOGIN_PASSWORD(0x01),
    SERVER_STATUS_REQUEST(0x06),
    SERVER_LIST_REQUEST(0x0B),
    PONG(0x18),
}