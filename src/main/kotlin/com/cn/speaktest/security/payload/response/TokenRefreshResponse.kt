package com.cn.speaktest.security.payload.response

data class TokenRefreshResponse(var accessToken: String, var refreshToken: String) {
    var tokenType = "Bearer"
}