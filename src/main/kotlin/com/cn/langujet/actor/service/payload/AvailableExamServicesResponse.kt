package com.cn.langujet.actor.service.payload

class AvailableExamServicesResponse(
    val id: String,
    val name: String,
    val price: Double,
    val discount: Double,
    var examVariant: ExamVariantResponse
)