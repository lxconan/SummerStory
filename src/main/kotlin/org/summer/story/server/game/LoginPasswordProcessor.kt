package org.summer.story.server.game

import org.slf4j.LoggerFactory
import org.summer.story.config.GlobalConfiguration
import org.summer.story.data.AccountRepository
import org.summer.story.net.encryption.HashAlgorithm
import org.summer.story.net.packet.InPacket
import org.summer.story.server.ReceiveOpcode
import org.summer.story.server.players.*
import java.nio.charset.Charset

class LoginPasswordProcessor(
    private val configuration: GlobalConfiguration,
    private val accountRepository: AccountRepository,
    private val hashAlgorithm: HashAlgorithm
) : GameProcessor {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginPasswordProcessor::class.java)
    }

    override fun getOpcode(): ReceiveOpcode = ReceiveOpcode.LOGIN_PASSWORD

    override fun process(player: Player, msg: InPacket) {
        val loginRequest = LoginPasswordInDto(msg, configuration.packet.charsetObject)
        val accountEntity = accountRepository.findAccountByName(loginRequest.accountName)
        if (accountEntity == null) {
            logger.debug("Account with name {} not found", loginRequest.accountName)
            player.declareLoginFailedForAccountNotFound()
            return
        }

        if (accountEntity.password != hashAlgorithm.sha256(loginRequest.password)) {
            logger.debug("Incorrect password for account {}, correct one is {}", loginRequest.accountName, hashAlgorithm.sha256(loginRequest.password))
            player.declareLoginFailedForIncorrectPassword()
            return
        }

        logger.debug("Account {} authenticated", loginRequest.accountName)
        player.setupAccountContext(accountEntity)
        player.declareAuthenticated(configuration.packet.charsetObject)
    }
}

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