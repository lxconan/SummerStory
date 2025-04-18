package org.summer.story.server.players

import org.summer.story.config.GlobalConfiguration
import org.summer.story.data.AccountEntity
import org.summer.story.server.login.game.WorldServerStatus
import org.summer.story.server.login.dtos.*
import org.summer.story.server.worlds.World
import java.nio.charset.Charset

fun Player.declareLoginFailedForIncorrectPassword() {
    this.respond(
        LoginFailedOutDto(LoginFailedOutDto.WellKnownLoginFailedReason.INCORRECT_PASSWORD.reason)
    )
}

fun Player.declareLoginFailedForAccountNotFound() {
    this.respond(
        LoginFailedOutDto(LoginFailedOutDto.WellKnownLoginFailedReason.ACCOUNT_NOT_FOUND.reason)
    )
}

fun Player.declareAuthenticated(charset: Charset) {
    requireNotNull(this.accountContext) { "Account context should have been initialized before this operation" }

    this.respond(
        LoginSuccessOutDto(this.accountContext!!.accountId, this.accountContext!!.accountName, charset)
    )
}

fun Player.sendWorldInformation(
    world: World,
    configuration: GlobalConfiguration
) {
    this.respond(WorldInformationOutDto(world, configuration))
}

fun Player.sendWorldInformationComplete() {
    this.respond(WorldInformationCompleteOutDto())
}

fun Player.selectLastConnectedWorld() {
    this.respond(LastConnectedWorldOutDto())
}

fun Player.selectRecommendedWorld() {
    this.respond(RecommendedWorldOutDto())
}

fun Player.setupAccountContext(accountEntity: AccountEntity) {
    this.accountContext = AccountContext(
        accountEntity.id,
        accountEntity.name,
        accountEntity.hardwareId
    )
}

fun Player.sendServerStatus(worldServerStatus: WorldServerStatus) {
    this.respond(ServerStatusOutDto(worldServerStatus))
}

fun Player.sendCannotFindAnyCharacters() {
    this.respond(CannotFindAnyCharactersOutDto())
}

fun Player.sendEmptyCharacterList() {
    this.respond(CharacterListOutDto(this.queryAvailableCharacterSlots()))
}

fun Player.sendCanCreateCharacter(name: String, forbidden: Boolean, charset: Charset) {
    this.respond(CharacterNameResponseOutDto(name, forbidden, charset))
}