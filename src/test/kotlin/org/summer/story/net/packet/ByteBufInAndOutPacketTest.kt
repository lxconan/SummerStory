package org.summer.story.net.packet

import io.netty.buffer.Unpooled
import kotlin.test.Test
import kotlin.test.assertEquals
import java.awt.Point
import java.nio.charset.StandardCharsets

class ByteBufInAndOutPacketTest {

  @Test
  fun `should write and read byte`() {
    val outPacket = ByteBufOutPacket()
    outPacket.writeByte(0x12)
    val inPacket = ByteBufInPacket(Unpooled.wrappedBuffer(outPacket.getBytes()))
    assertEquals(0x12.toByte(), inPacket.readByte())
  }

  @Test
  fun `should write and read short`() {
    val outPacket = ByteBufOutPacket()
    outPacket.writeShort(0x1234)
    val inPacket = ByteBufInPacket(Unpooled.wrappedBuffer(outPacket.getBytes()))
    assertEquals(0x1234.toShort(), inPacket.readShort())
  }

  @Test
  fun `should write and read int`() {
    val outPacket = ByteBufOutPacket()
    outPacket.writeInt(0x12345678)
    val inPacket = ByteBufInPacket(Unpooled.wrappedBuffer(outPacket.getBytes()))
    assertEquals(0x12345678, inPacket.readInt())
  }

  @Test
  fun `should write and read long`() {
    val outPacket = ByteBufOutPacket()
    outPacket.writeLong(0x123456789ABCDEF0)
    val inPacket = ByteBufInPacket(Unpooled.wrappedBuffer(outPacket.getBytes()))
    assertEquals(0x123456789ABCDEF0, inPacket.readLong())
  }

  @Test
  fun `should write and read boolean`() {
    val outPacket = ByteBufOutPacket()
    outPacket.writeBool(true)
    val inPacket = ByteBufInPacket(Unpooled.wrappedBuffer(outPacket.getBytes()))
    assertEquals(true, inPacket.readByte() == 1.toByte())
  }

  @Test
  fun `should write and read string`() {
    val outPacket = ByteBufOutPacket()
    val testString = "Hello, World!"
    outPacket.writeString(testString, StandardCharsets.UTF_8)
    val inPacket = ByteBufInPacket(Unpooled.wrappedBuffer(outPacket.getBytes()))
    assertEquals(testString, inPacket.readString(StandardCharsets.UTF_8))
  }

  @Test
  fun `should write and read position`() {
    val outPacket = ByteBufOutPacket()
    val point = Point(123, 456)
    outPacket.writePos(point)
    val inPacket = ByteBufInPacket(Unpooled.wrappedBuffer(outPacket.getBytes()))
    assertEquals(point, inPacket.readPos())
  }
}