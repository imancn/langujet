package com.cn.langujet.domain.student.model

import com.cn.langujet.application.security.security.model.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "students")
data class Student(
    @Id
    var id: String?,
    @DBRef
    var user: User,
    var fullName: String,
    var biography: String?,
) {
    constructor(user: User, fullName: String?) : this(
        null,
        user,
        fullName ?: user.email,
        null
    )
}