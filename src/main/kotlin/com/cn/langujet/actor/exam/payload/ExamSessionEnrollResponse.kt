package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import java.util.*

class ExamSessionEnrollResponse(
    val examSessionId: String?,
    val enrollDate: Date,
    val sessionState: ExamSessionState,
    val sectionOrders: List<Int>
) {
    constructor(
        examSession: ExamSession,
    ):this (
        examSession.id,
        examSession.enrollDate,
        examSession.state,
        examSession.sectionOrders
    )
}
