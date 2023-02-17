package com.cn.speaktest.payload.request.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignupRequest(
    var fullName: String?,
    @field:NotBlank @field:Size(max = 50) @field:Email var email: String,
    @field:NotBlank @field:Size(min = 6, max = 40) var password: String,
)