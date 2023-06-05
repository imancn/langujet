package com.cn.speaktest.actor.professor.payload.response

import com.cn.speaktest.application.security.security.model.User
import com.cn.speaktest.domain.professor.Professor

data class ProfessorProfileResponse(
    var id: String?,
    var user: User,
    var fullName: String,
    var biography: String?,
    var ieltsScore: Double?,
    var credit: Double = 0.0,
) {
    constructor(professor: Professor) : this(
        professor.id,
        professor.user,
        professor.fullName,
        professor.biography,
        professor.ieltsScore,
        professor.credit
    )
}