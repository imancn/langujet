package com.cn.langujet.actor.result.payload.request

data class AddCorrectorSectionResultRequest(
    val correctionId: String,
    val score: Double,
    val recommendation: String
)
