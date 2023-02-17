package com.cn.speaktest.security.payload.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignInRequest(
    @field:NotBlank @field:Email var email: String,
    @field:NotBlank var password: String,
)