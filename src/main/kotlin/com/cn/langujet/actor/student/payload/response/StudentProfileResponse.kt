package com.cn.langujet.actor.student.payload.response

import com.cn.langujet.domain.student.model.Student

data class StudentProfileResponse(
    var email: String,
    var fullName: String,
    var biography: String?
) {
    constructor(student: Student) : this(
        email = student.user.email,
        fullName = student.fullName,
        biography = student.biography
    )
}
