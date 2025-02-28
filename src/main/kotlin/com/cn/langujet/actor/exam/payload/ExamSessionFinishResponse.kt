package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.enums.ExamSessionState

data class ExamSessionFinishResponse (
    val examSessionState: ExamSessionState,
)