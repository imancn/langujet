package com.cn.langujet.domain.corrector

import com.cn.langujet.domain.user.model.User
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "correctors")
@TypeAlias("correctors")
data class Corrector(
    @Id
    var id: String?,
    @DBRef
    var user: User,
    var fullName: String,
    var biography: String?,
    var ieltsScore: Double?,
    var credit: Double = 0.0,
) {
    constructor(user: User, fullName: String) : this(
        null,
        user,
        fullName,
        null,
        null,
        0.0
    )
}