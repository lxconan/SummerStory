package org.summer.story.server

import io.netty.channel.socket.SocketChannel

class NetworkContext(channel: SocketChannel) {
    val clientIp: String? = channel.remoteAddress()?.hostString

    fun isValid(): Boolean {
        return clientIp != null
    }
}