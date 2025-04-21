package com.cn.langujet.actor.correction.payload.request

data class AssignSpecificCorrectionToCorrectorRequest(
    val examSessionId: Long,
    val correctorUserId: Long,
)
