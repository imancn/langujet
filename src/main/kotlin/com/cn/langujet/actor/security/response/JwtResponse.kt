package com.cn.langujet.actor.security.response

data class JwtResponse(
    var accessToken: String,
    var refreshToken: String,
    var email: String?,
) {
    var tokenType = "Bearer"
}