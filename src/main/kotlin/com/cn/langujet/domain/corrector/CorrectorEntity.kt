package com.cn.langujet.domain.corrector

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.domain.user.model.UserEntity
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "correctors")
@TypeAlias("correctors")
class CorrectorEntity(
    id: Long?,
    @DBRef
    var user: UserEntity,
    var fullName: String,
    var ieltsScore: Double,
    var biography: String?,
) : HistoricalEntity(id = id) {
    constructor(user: UserEntity, fullName: String, ieltsScore: Double) : this(
        null,
        user,
        fullName,
        ieltsScore,
        null,
    )
}