package com.cn.speaktest.security.payload.response

import com.cn.speaktest.model.Student

data class StudentProfileResponse(
    var id: String?,
    var email: String,
    var fullName: String,
    var biography: String?,
    var credit: Double = 0.0,
) {
    constructor(student: Student) : this(
        id = student.id,
        email = student.user.email,
        fullName = student.fullName,
        biography = student.biography,
        credit = student.credit
    )
}
