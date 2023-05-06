package com.cn.speaktest.application.security.security.payload.response

data class RefreshTokenResponse(var accessToken: String, var refreshToken: String) {
    var tokenType = "Bearer"
}