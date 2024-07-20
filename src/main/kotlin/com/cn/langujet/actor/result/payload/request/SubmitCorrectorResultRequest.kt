package com.cn.langujet.actor.result.payload.request

data class SubmitCorrectorResultRequest(
    val resultId: String,
    val recommendation: String
)
