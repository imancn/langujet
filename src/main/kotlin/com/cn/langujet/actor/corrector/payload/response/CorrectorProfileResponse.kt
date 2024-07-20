package com.cn.langujet.actor.corrector.payload.response

import com.cn.langujet.domain.user.model.UserEntity
import com.cn.langujet.domain.corrector.CorrectorEntity

data class CorrectorProfileResponse(
    var id: String?,
    var user: UserEntity,
    var fullName: String,
    var biography: String?,
    var ieltsScore: Double?,
    var credit: Double = 0.0,
) {
    constructor(corrector: CorrectorEntity) : this(
        corrector.id,
        corrector.user,
        corrector.fullName,
        corrector.biography,
        corrector.ieltsScore,
        corrector.credit
    )
}