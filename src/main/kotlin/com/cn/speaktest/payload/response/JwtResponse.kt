package com.cn.speaktest.payload.response

data class JwtResponse(
    var accessToken: String,
    var id: String?,
    var username: String,
    var refreshToken: String,
    var email: String?,
    val roles: List<String>
) {
    var tokenType = "Bearer"

}