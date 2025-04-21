package com.cn.langujet.actor.corrector.payload.response

import com.cn.langujet.domain.corrector.CorrectorEntity
import com.cn.langujet.domain.user.model.UserEntity

data class CorrectorProfileResponse(
    var id: Long?,
    var user: UserEntity,
    var fullName: String,
    var biography: String?,
    var ieltsScore: Double?,
) {
    constructor(corrector: CorrectorEntity) : this(
        corrector.id,
        corrector.user,
        corrector.fullName,
        corrector.biography,
        corrector.ieltsScore,
    )
}