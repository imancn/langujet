package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.correction.model.CorrectionType
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

class ExamSessionResponse (
    val examSessionId: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val exam: ExamDTO?,
    val sectionOrders: List<Int>,
    val correctionType: CorrectionType?,
    val state: ExamSessionState,
    val enrollDate: Date,
    val startDate: Date?,
    val endDate: Date?,
    val correctionDate: Date?
) {
    constructor(
        examSession: ExamSession,
        exam: ExamDTO?,
        correctionType: CorrectionType?
    ):this (
        examSession.id ?: "",
        exam.also { it?.id = null },
        examSession.sectionOrders,
        correctionType,
        examSession.state,
        examSession.enrollDate,
        examSession.startDate,
        examSession.endDate,
        examSession.correctionDate
    )
}
