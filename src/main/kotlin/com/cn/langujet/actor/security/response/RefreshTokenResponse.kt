package com.cn.langujet.application.security.security.payload.response

data class RefreshTokenResponse(var accessToken: String, var refreshToken: String) {
    var tokenType = "Bearer"
}