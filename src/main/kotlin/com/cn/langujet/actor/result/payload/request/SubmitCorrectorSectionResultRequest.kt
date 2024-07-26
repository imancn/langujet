package com.cn.langujet.actor.result.payload.request

data class SubmitCorrectorSectionResultRequest(
    val sectionCorrectionId: String,
    val score: Double,
    val recommendation: String
)
