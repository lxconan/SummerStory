package org.summer.story.server

enum class ReceiveOpcode(val value: Int = -2) {
    LOGIN_PASSWORD(0x01),
    PONG(0x18)
}