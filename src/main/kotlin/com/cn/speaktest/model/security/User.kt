package com.cn.speaktest.model.security

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Document(collection = "users")
data class User(
    @Id
    var id: String?,
    var username: @NotBlank @Size(max = 20) String,
    var email: @NotBlank @Size(max = 50) @Email String,
    var password: @NotBlank @Size(max = 120) String,
    var roles: Set<Role> = HashSet()
)