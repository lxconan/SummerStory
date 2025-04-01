package org.summer.story.server.dtos

import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.packet.ByteBufOutPacket
import org.summer.story.server.SendOpcode
import org.summer.story.server.worlds.World

class WorldInformationOutDto(
    val world: World,
    val configuration: GlobalConfiguration
) : OutDto() {
    override fun toString(): String = "[World Information]"

    override fun writePacket(packet: ByteBufOutPacket) {
        packet.writeShort(SendOpcode.SERVER_LIST.value)
        packet.writeByte(world.id - 1) // world id
        packet.writeString(world.name, configuration.packet.charsetObject)
        packet.writeByte(world.flag)
        packet.writeString(world.eventMessage, configuration.packet.charsetObject)
        packet.writeByte(100) // rate modifier, don't know why
        packet.writeByte(0) // event experience, don't know why
        packet.writeByte(100) // rate modifier, don't know why
        packet.writeByte(0) // event drop, don't know why
        packet.writeByte(0) // don't know what it is
        packet.writeByte(world.channels.size)
        for (gameChannel in world.channels) {
            packet.writeString(
                world.name + "-" + gameChannel.id,
                configuration.packet.charsetObject
            )
            packet.writeInt(gameChannel.playerCapacity)

            packet.writeByte(world.id) // maybe world id, should be checked!
            packet.writeByte(gameChannel.id - 1) // channel id
            packet.writeBool(false) // adult channel
        }
        packet.writeShort(0) // packet end
    }
}