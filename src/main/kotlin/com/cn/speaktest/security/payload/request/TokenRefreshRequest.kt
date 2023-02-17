package com.cn.speaktest.security.payload.request

import jakarta.validation.constraints.NotBlank

data class TokenRefreshRequest (
    @field:NotBlank var refreshToken: String
)