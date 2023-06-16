package com.cn.langujet.application.security.security.payload.response

data class JwtResponse(
    var accessToken: String,
    var refreshToken: String,
    var email: String?,
) {
    var tokenType = "Bearer"
}