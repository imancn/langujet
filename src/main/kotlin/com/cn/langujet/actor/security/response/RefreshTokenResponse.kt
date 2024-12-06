package com.cn.langujet.actor.security.response

data class RefreshTokenResponse(var accessToken: String, var refreshToken: String) {
    var tokenType = "Bearer"
}