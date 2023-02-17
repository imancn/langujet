package com.cn.speaktest.student.model

import com.cn.speaktest.security.model.User
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
    var credit: Double = 0.0,
) {
    constructor(user: User, fullName: String, biography: String?) : this(null, user, fullName, biography, 0.0)

    constructor(user: User, fullName: String?) : this(
        null,
        user,
        fullName ?: user.email,
        null,
        0.0
    )
}