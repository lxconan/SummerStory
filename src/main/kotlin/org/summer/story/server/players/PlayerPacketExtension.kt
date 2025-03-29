package org.summer.story.server.players

import org.summer.story.server.dtos.LoginFailedOutDto

fun Player.declareLoginFailedForIncorrectPassword() {
    this.respond(
        LoginFailedOutDto(LoginFailedOutDto.WellKnownLoginFailedReason.INCORRECT_PASSWORD.reason))
}