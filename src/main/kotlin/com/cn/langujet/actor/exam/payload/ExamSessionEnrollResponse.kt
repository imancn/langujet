package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.enums.ExamSessionState
import java.util.*

class ExamSessionEnrollResponse(
    val examSessionId: Long?,
    val enrollDate: Date,
    val sessionState: ExamSessionState,
) {
    constructor(
        examSession: ExamSessionEntity,
    ):this (
        examSession.id,
        examSession.enrollDate,
        examSession.state,
    )
}
