package com.cn.langujet.actor.correction.payload.response

import com.cn.langujet.domain.exam.model.ExamMode
import com.cn.langujet.domain.exam.model.ExamType

class CorrectorAvailableCorrectionResponse (
    val examType: ExamType,
    val examMode: ExamMode,
    var count: Int
)