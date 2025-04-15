# Unit Test Instructions

The unit test should use end-2-end test whenever possible. To enable dependency injection support and in-memory database support, Please use the [DatabaseEnabledTest](/src/test/kotlin/org/summer/story/DatabaseEnabledTest.kt) class as the base class. It will enable you:

* A clean migrated in-memory database before each test.
* A full functional `koin` container, with customization support (overriding `updateKoinModules()` method)

## Test Rules

### Rule 1: Test Name

We prefer using the following pattern as the test name: `should ...`. And please do **NOT** use the camel cased patterns. The following shows an example:

```kotlin
@Test
fun `should fail login for non-existent account`() {
    // ...
}
```

### Rule 2: Using Mocked Player

This rule is only valid when creating unit test for `GameProcessor` derived classes.

The `player` object is really useful when we testing `GameProcessors`. You can use the following way to create a mock player. The definition of the `MockPlayer` is at [MockPlayer.kt](/src/test/kotlin/org/summer/story/MockPlayer.kt).

```kotlin
private fun createMockPlayer(): MockPlayer {
    val channel = mockk<SocketChannel>()
    every {channel.isActive} returns true
    every {channel.remoteAddress()} returns InetSocketAddress(Inet4Address.getLoopbackAddress(), 12345)
    val internal = PlayerImpl(koin.get(), koin.get(), NetworkContext(channel))
    return MockPlayer(internal)
}
```

Then you can pass the created `player` as the first argument of `GameProcessor.process(player: Player, msg: InPacket)` method, after the `process` method completes, you can verify `MockPlayer.responds`. It records all the arguments for each call to `MockPlayer.respond` methods. For example, in [LoginPasswordProcessorTest.kt](/src/test/kotlin/org/summer/story/server/login/game/LoginPasswordProcessorTest.kt), we have the following test:

```kotlin
@Test
fun `should fail login for non-existent account`() {
    // Given
    val accountName = "nonexistent"
    val password = "password"
    val player = createMockPlayer()

    // When
    val processor = koin.get<LoginPasswordProcessor>()
    processor.process(player, InPacketFactory.createLoginPasswordWithoutOpcode(
        accountName, password, 0, configuration.packet.charsetObject))

    // Then
    player.responds.first().let {
        val dto = it as LoginFailedOutDto
        assertEquals(dto.reason, LoginFailedOutDto.WellKnownLoginFailedReason.ACCOUNT_NOT_FOUND.reason)
    }
}
```

### Rule 3: Construct InPacket

If we have to create corresponding `InPacket`. Please create a factory method in [InPacketFactory.kt](/src/test/kotlin/org/summer/story/InPacketFactory.kt), or reuse existed method. The factory method should not include the opcode since the opcode should have been read from the buffer. The way we create `InPacket` can be refrenced from the corresponding `OutPacket` implementation. For example, for 

```kotlin
@Suppress("JoinDeclarationAndAssignment")
class LoginPasswordInDto(msg: InPacket, charset: Charset) {
    val accountName: String
    val password: String
    val hardwareId: ByteArray

    init {
        accountName = msg.readString(charset)
        password = msg.readString(charset)
        msg.skip(6) // network address masks
        hardwareId = msg.readBytes(4)
    }
}
```

We can create the corresponding factory method in the `InPacketFactory` class:

```kotlin
fun createLoginPasswordWithoutOpcode(accountName: String, password: String, hardwareId: Int, charset: Charset): InPacket {
    val bytes = ByteBufOutPacket().apply {
        writeString(accountName, charset)
        writeString(password, charset)
        writeBytes(byteArrayOf(0, 0, 0, 0, 0, 0)) // network address masks
        writeInt(hardwareId)
    }.getBytes()
    return ByteBufInPacket(Unpooled.wrappedBuffer(bytes))
}
```

And then use it in the test, for example:

```kotlin
val processor = koin.get<LoginPasswordProcessor>()
processor.process(player, InPacketFactory.createLoginPasswordWithoutOpcode(
    accountName, password, 0, configuration.packet.charsetObject))
```
