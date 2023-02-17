package com.cn.speaktest.security.payload.response

data class JwtResponse(
    var accessToken: String,
    var refreshToken: String,
    var email: String?,
) {
    var tokenType = "Bearer"
}