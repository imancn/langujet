package com.cn.langujet.actor.correction.payload.request

import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType

data class AssignCorrectionRequest(
    val examType: ExamType,
    val examMode: ExamMode,
)