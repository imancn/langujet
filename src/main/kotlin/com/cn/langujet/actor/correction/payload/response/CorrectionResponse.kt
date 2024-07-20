package com.cn.langujet.actor.correction.payload.response

import com.cn.langujet.domain.exam.model.ExamMode
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType

data class CorrectionResponse(
    val examCorrectionId: String,
    val examType: ExamType,
    val examMode: ExamMode,
    val sections: List<CorrectionSectionResponse>,
)

data class CorrectionSectionResponse(
    val sectionCorrectionId: String?,
    val sectionType: SectionType,
    val sectionOrder: Int
)
