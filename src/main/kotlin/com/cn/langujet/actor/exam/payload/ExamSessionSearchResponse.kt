package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import java.util.*

class ExamSessionSearchResponse (
    val examSessionId: String,
    val exam: ExamDTO?,
    val sectionOrders: List<Int>,
    val correctorType: CorrectorType?,
    val state: ExamSessionState,
    val enrollDate: Date,
    val startDate: Date?,
    val endDate: Date?,
    val correctionDate: Date?
) {
    constructor(
        examSession: ExamSession,
        exam: ExamDTO?,
        correctorType: CorrectorType?
    ):this (
        examSession.id ?: "",
        exam.also { it?.id = null },
        examSession.sectionOrders,
        correctorType,
        examSession.state,
        examSession.enrollDate,
        examSession.startDate,
        examSession.endDate,
        examSession.correctionDate
    )
}
