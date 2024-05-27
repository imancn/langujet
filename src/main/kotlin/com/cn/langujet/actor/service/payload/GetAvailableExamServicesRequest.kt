package com.cn.langujet.actor.service.payload

import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamType

class GetAvailableExamServicesRequest(
    val correctorType: List<CorrectorType>?,
    val examType: List<ExamType>?,
    val hasDiscount: Boolean?,
    val pageSize: Int,
    val pageNumber: Int
)
