package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType

data class ExamRequest(
    var examType: ExamType,
    var sectionType: SectionType?,
)