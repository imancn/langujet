package com.cn.langujet.domain.user.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    var id: String?,
    var email: String,
    var emailVerified: Boolean,
    var password: String,
    var roles: Set<Role> = HashSet()
)