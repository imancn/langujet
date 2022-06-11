package com.cn.speaktest.payload.response

data class TokenRefreshResponse(var accessToken: String, var refreshToken: String) {
    var tokenType = "Bearer"

}