package org.summer.story

import io.netty.buffer.Unpooled
import org.summer.story.net.packet.ByteBufInPacket
import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.net.packet.InPacket
import java.nio.charset.Charset

object InPacketFactory {
    fun createLoginPasswordWithoutOpcode(accountName: String, password: String, hardwareId: Int, charset: Charset): InPacket {
        val bytes = ByteBufOutPacket().apply {
            writeString(accountName, charset)
            writeString(password, charset)
            writeBytes(byteArrayOf(0, 0, 0, 0, 0, 0)) // network address masks
            writeInt(hardwareId)
        }.getBytes()
        return ByteBufInPacket(Unpooled.wrappedBuffer(bytes))
    }

    fun createServerListRequestWithoutOpcode(): InPacket {
        return ByteBufInPacket(Unpooled.EMPTY_BUFFER)
    }

    fun createServerStatusRequestWithoutOpcode(worldIndex: Int): InPacket {
        val bytes = ByteBufOutPacket().apply {
            writeShort(worldIndex)
        }.getBytes()
        return ByteBufInPacket(Unpooled.wrappedBuffer(bytes))
    }

    fun createCharacterListRequestWithoutOpcode(worldId: Int, channelId: Int): InPacket {
        val bytes = ByteBufOutPacket().apply {
            writeByte(0) // should be skipped
            writeByte(worldId)
            writeByte(channelId)
        }.getBytes()
        return ByteBufInPacket(Unpooled.wrappedBuffer(bytes))
    }

    fun createCharacterNameRequestWithoutOpCode(validName: String, charset: Charset): InPacket {
        val bytes = ByteBufOutPacket().apply {
            writeString(validName, charset)
        }.getBytes()
        return ByteBufInPacket(Unpooled.wrappedBuffer(bytes))
    }
}