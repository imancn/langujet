package com.cn.speaktest.payload.request.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignInRequest(
    @field:NotBlank @field:Email var email: String,
    @field:NotBlank var password: String,
)