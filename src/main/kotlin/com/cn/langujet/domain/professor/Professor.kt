package com.cn.langujet.domain.professor

import com.cn.langujet.actor.professor.payload.response.ProfessorProfileResponse
import com.cn.langujet.domain.user.model.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "professors")
data class Professor(
    @Id
    var id: String?,
    @DBRef
    var user: User,
    var fullName: String,
    var biography: String?,
    var ieltsScore: Double?,
    var credit: Double = 0.0,
) {
    constructor(
        user: User,
        fullName: String,
        biography: String?,
        ieltsScore: Double?,
    ) : this(
        null,
        user,
        fullName,
        biography,
        ieltsScore,
        0.0
    )

    constructor(user: User, fullName: String?) : this(
        null,
        user,
        fullName ?: user.email,
        null,
        null,
        0.0
    )

    constructor(professor: ProfessorProfileResponse) : this(
        professor.id,
        professor.user,
        professor.fullName,
        professor.biography,
        professor.ieltsScore,
        professor.credit
    )
}