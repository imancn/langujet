package com.cn.langujet.actor.correction.payload.response

import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType

data class CorrectionResponse(
    val examType: ExamType,
    val sectionTypes: List<SectionType>,
)
