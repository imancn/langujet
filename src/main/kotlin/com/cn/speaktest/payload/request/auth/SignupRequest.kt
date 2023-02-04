package com.cn.speaktest.payload.request.auth

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignupRequest(
    var fullName: String?,
    @field:NotBlank @field:Size(max = 50) @field:Email var email: String,
    @field:NotBlank @field:Size(min = 6, max = 40) var password: String,
)