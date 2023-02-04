package com.cn.speaktest.payload.request.auth

import javax.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank var email: String,
    @field:NotBlank var password: String,
)