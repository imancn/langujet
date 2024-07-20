package com.cn.langujet.actor.student.payload.response

import com.cn.langujet.domain.student.model.StudentEntity

data class StudentProfileResponse(
    var email: String,
    var fullName: String,
    var biography: String?
) {
    constructor(student: StudentEntity) : this(
        email = student.user.email,
        fullName = student.fullName,
        biography = student.biography
    )
}
