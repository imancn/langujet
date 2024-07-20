package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.ExamSessionState
import java.util.*

class ExamSessionEnrollResponse(
    val examSessionId: String?,
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
