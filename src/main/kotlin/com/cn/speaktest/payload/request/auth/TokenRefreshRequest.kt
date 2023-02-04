package com.cn.speaktest.payload.request.auth

import javax.validation.constraints.NotBlank

data class TokenRefreshRequest (
    @field:NotBlank var refreshToken: String
)