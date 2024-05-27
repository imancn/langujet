package com.cn.langujet.actor.service.payload

class GetAvailableExamServicesResponse(
    val id: String,
    val name: String,
    val price: Double,
    val discount: Double,
    val examVariant: ExamVariantResponse?,
    val count: Int
)