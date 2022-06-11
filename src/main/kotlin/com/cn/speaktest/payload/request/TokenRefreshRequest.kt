package com.cn.speaktest.payload.request

import javax.validation.constraints.NotBlank

data class TokenRefreshRequest (
    var refreshToken: @NotBlank String
)