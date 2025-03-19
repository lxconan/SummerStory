package org.summer.story.server

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ReplayingDecoder
import org.slf4j.LoggerFactory
import org.summer.story.net.encryption.MapleAesOfb
import org.summer.story.net.encryption.MapleCustomEncryption
import org.summer.story.net.packet.ByteBufInPacket
import org.summer.story.net.packet.InvalidPacketException

class PacketDecoder(private val receiveCypher: MapleAesOfb) : ReplayingDecoder<Void>() {
    companion object {
        private val logger = LoggerFactory.getLogger(PacketDecoder::class.java)
    }

    override fun decode(ctx: ChannelHandlerContext?, msg: ByteBuf?, out: MutableList<Any>?) {
        requireNotNull(msg) { "ByteBuf cannot be null" }
        requireNotNull(out) { "List cannot be null" }

        val header: Int = msg.readInt()
        if (!receiveCypher.isValidHeader(header)) {
            throw InvalidPacketException(header)
        }

        val packetLength: Int = decodePacketLength(header)
        val raw = ByteArray(packetLength)
        msg.readBytes(raw)

        receiveCypher.crypt(raw)
        MapleCustomEncryption.decryptData(raw)
        val packet = ByteBufInPacket(Unpooled.wrappedBuffer(raw))
        out.add(packet)
    }

    private fun decodePacketLength(header: Int): Int {
        var length = (header ushr 16) xor (header and 0xFFFF)
        length = ((length shl 8) and 0xFF00) or ((length ushr 8) and 0xFF)
        return length
    }
}