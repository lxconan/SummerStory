package org.summer.story.server

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.encryption.MapleAesOfb
import org.summer.story.net.encryption.MapleCustomEncryption
import org.summer.story.net.packet.Packet

class PacketEncoder(private val sendCypher: MapleAesOfb, configuration: GlobalConfiguration) : MessageToByteEncoder<Packet>() {
    override fun encode(ctx: ChannelHandlerContext?, msg: Packet?, out: ByteBuf?) {
        requireNotNull(msg) { "Packet cannot be null" }
        requireNotNull(out) { "ByteBuf cannot be null" }

        val packet: ByteArray = msg.getBytes()
        val encodedHeader = sendCypher.getPacketHeader(packet.size)
        out.writeBytes(encodedHeader)

        MapleCustomEncryption.encryptData(packet)
        sendCypher.crypt(packet)
        out.writeBytes(packet)
    }
}
