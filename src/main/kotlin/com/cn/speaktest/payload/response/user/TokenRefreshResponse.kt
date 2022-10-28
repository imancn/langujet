package com.cn.speaktest.payload.response.user

data class TokenRefreshResponse(var accessToken: String, var refreshToken: String) {
    var tokenType = "Bearer"
}