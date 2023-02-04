package com.cn.speaktest.payload.request.auth

import javax.validation.constraints.NotBlank

data class LoginRequest(
    var email: @NotBlank String,
    var password: @NotBlank String,
)