package com.cn.langujet.domain.student.model

import com.cn.langujet.application.shared.HistoricalEntity
import com.cn.langujet.domain.user.model.UserEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "students")
@TypeAlias("students")
data class StudentEntity(
    @Id
    var id: String?,
    @DBRef
    var user: UserEntity,
    var fullName: String,
    var biography: String?,
): HistoricalEntity() {
    constructor(user: UserEntity, fullName: String) : this(
        null,
        user,
        fullName,
        null
    )
}