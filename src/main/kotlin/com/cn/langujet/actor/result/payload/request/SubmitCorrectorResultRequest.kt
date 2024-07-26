package com.cn.langujet.actor.result.payload.request

data class SubmitCorrectorResultRequest(
    val examCorrectionId: String,
    val recommendation: String
)
