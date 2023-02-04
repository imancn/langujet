package com.cn.speaktest.payload.request.auth

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignupRequest(
    var fullName: String?,
    var email: @NotBlank @Size(max = 50) @Email String,
    var password: @NotBlank @Size(min = 6, max = 40) String,
)