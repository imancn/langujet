package com.cn.speaktest.payload.request.auth

import jakarta.validation.constraints.NotBlank

data class TokenRefreshRequest (
    @field:NotBlank var refreshToken: String
)