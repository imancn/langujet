package com.cn.langujet.actor.correction.payload.request

import com.cn.langujet.domain.exam.model.ExamMode
import com.cn.langujet.domain.exam.model.ExamType

data class AssignCorrectionRequest(
    val examType: ExamType,
    val examMode: ExamMode,
)