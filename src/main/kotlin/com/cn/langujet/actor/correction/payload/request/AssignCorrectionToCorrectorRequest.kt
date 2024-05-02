package com.cn.langujet.actor.correction.payload.request

import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType

data class AssignCorrectionToCorrectorRequest(
    val examType: ExamType,
    val sectionTypes: List<SectionType>,
    val correctorId: String
)
