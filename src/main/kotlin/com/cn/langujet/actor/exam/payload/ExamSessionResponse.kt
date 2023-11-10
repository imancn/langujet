package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import java.util.*

class ExamSessionResponse (
    val examSessionId: String,
    val exam: ExamDTO?,
    val sectionOrders: List<Int>,
    val state: ExamSessionState,
    val enrollDate: Date,
    val startDate: Date?,
    val endDate: Date?,
    val rateDate: Date?
) {
    constructor(
        examSession: ExamSession,
        exam: ExamDTO?,
    ):this (
        examSession.id ?: "",
        exam.also { it?.id = null },
        examSession.sectionOrders,
        examSession.state,
        examSession.enrollDate,
        examSession.startDate,
        examSession.endDate,
        examSession.rateDate
    )
}
