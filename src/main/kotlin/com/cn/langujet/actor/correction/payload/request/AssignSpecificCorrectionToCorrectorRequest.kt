package com.cn.langujet.actor.correction.payload.request

data class AssignSpecificCorrectionToCorrectorRequest(
    val examSessionId: String,
    val correctorUserId: String,
)
