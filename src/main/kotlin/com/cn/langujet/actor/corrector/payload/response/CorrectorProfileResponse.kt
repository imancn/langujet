package com.cn.langujet.actor.corrector.payload.response

import com.cn.langujet.domain.user.model.User
import com.cn.langujet.domain.corrector.Corrector

data class CorrectorProfileResponse(
    var id: String?,
    var user: User,
    var fullName: String,
    var biography: String?,
    var ieltsScore: Double?,
    var credit: Double = 0.0,
) {
    constructor(corrector: Corrector) : this(
        corrector.id,
        corrector.user,
        corrector.fullName,
        corrector.biography,
        corrector.ieltsScore,
        corrector.credit
    )
}