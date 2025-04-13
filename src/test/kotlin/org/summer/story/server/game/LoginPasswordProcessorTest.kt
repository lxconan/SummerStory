package org.summer.story.server.game

import io.mockk.every
import io.mockk.mockk
import io.netty.channel.socket.SocketChannel
import org.junit.jupiter.api.Test
import org.summer.story.DatabaseEnabledTest
import org.summer.story.InPacketFactory
import org.summer.story.MockPlayer
import org.summer.story.server.NetworkContext
import org.summer.story.server.login.dtos.LoginFailedOutDto
import org.summer.story.server.login.dtos.LoginSuccessOutDto
import org.summer.story.server.login.game.LoginPasswordProcessor
import org.summer.story.server.players.PlayerImpl
import java.net.Inet4Address
import java.net.InetSocketAddress
import kotlin.test.assertEquals

class LoginPasswordProcessorTest : DatabaseEnabledTest() {
    private fun createMockPlayer(): MockPlayer {
        val channel = mockk<SocketChannel>()
        every {channel.isActive} returns true
        every {channel.remoteAddress()} returns InetSocketAddress(Inet4Address.getLoopbackAddress(), 12345)
        val internal = PlayerImpl(koin.get(), koin.get(), NetworkContext(channel))
        return MockPlayer(internal)
    }

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

    @Test
    fun `should fail login for incorrect password`() {
        // Given
        val accountName = "existing"
        val password = "wrongpassword"
        val correctPassword = "correctpassword"
        givenAccount(accountName, correctPassword)
        val player = createMockPlayer()

        // When
        val processor = koin.get<LoginPasswordProcessor>()
        processor.process(player, InPacketFactory.createLoginPasswordWithoutOpcode(
            accountName, password, 0, configuration.packet.charsetObject))

        // Then
        player.responds.first().let {
            val dto = it as LoginFailedOutDto
            assertEquals(dto.reason, LoginFailedOutDto.WellKnownLoginFailedReason.INCORRECT_PASSWORD.reason)
        }
    }

    @Test
    fun `should authenticate valid account`() {
        // Given
        val accountName = "existing"
        val password = "password"
        val accountId: Int = givenAccount(accountName, password)
        val player = createMockPlayer()

        // When
        val processor = koin.get<LoginPasswordProcessor>()
        processor.process(player, InPacketFactory.createLoginPasswordWithoutOpcode(
            accountName, password, 0, configuration.packet.charsetObject))

        // Then
        player.responds.first().let {
            val dto = it as LoginSuccessOutDto
            assertEquals(accountName, dto.accountName)
            assertEquals(accountId, dto.accountId)
        }
    }
}
