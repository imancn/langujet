package com.cn.speaktest.payload.response.user

data class JwtResponse(
    var accessToken: String,
    var refreshToken: String,
    var email: String?,
) {
    var tokenType = "Bearer"
}