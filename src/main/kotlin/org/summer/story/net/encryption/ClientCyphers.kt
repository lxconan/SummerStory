package org.summer.story.net.encryption

import org.summer.story.server.ServerMetadata

class ClientCyphers(private val send: MapleAesOfb, private val receive: MapleAesOfb) {
    companion object {
        fun create(iv: IvPair): ClientCyphers {
            val send = MapleAesOfb(iv.send, (0xFFFF - ServerMetadata.SERVER_VERSION).toShort())
            val receive = MapleAesOfb(iv.receive, ServerMetadata.SERVER_VERSION.toShort())
            return ClientCyphers(send, receive)
        }
    }

    fun getSendCypher(): MapleAesOfb = send
    fun getReceiveCypher(): MapleAesOfb = receive
}