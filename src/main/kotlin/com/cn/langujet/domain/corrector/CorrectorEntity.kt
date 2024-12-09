package com.cn.langujet.domain.corrector

import com.cn.langujet.application.shared.LogEntity
import com.cn.langujet.domain.user.model.UserEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "correctors")
@TypeAlias("correctors")
data class CorrectorEntity(
    @Id
    var id: String?,
    @DBRef
    var user: UserEntity,
    var fullName: String,
    var ieltsScore: Double,
    var biography: String?,
): LogEntity() {
    constructor(user: UserEntity, fullName: String, ieltsScore: Double) : this(
        null,
        user,
        fullName,
        ieltsScore,
        null,
    )
}