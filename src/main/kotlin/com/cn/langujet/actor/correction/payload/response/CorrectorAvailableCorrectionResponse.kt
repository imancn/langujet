package com.cn.langujet.actor.correction.payload.response

import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType

class CorrectorAvailableCorrectionResponse (
    val examType: ExamType,
    val sectionTypes: List<SectionType>,
    var count: Int
)