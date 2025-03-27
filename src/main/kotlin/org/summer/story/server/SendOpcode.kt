package org.summer.story.server

enum class SendOpcode(val value: Int = -2) {
    LOGIN_STATUS(0x00),
    PING(0x11)
}

