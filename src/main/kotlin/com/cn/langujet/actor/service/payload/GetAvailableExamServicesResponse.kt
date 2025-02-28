package com.cn.langujet.actor.service.payload

import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType

class GetAvailableExamServicesResponse(
    val id: String,
    val name: String,
    val price: Double,
    val discount: Double,
    var examType: ExamType,
    var examMode: ExamMode,
    var correctorType: CorrectorType,
    val count: Int
)