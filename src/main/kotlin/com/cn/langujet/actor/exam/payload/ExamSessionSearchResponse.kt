package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.*
import java.util.*

class ExamSessionSearchResponse (
    val examSessionId: String,
    val exam: ExamSessionExamSearchResponse?,
    val correctorType: CorrectorType?,
    val state: ExamSessionState,
    val enrollDate: Date,
    val startDate: Date?,
    val endDate: Date?,
    val correctionDate: Date?
) {
    constructor(
        examSession: ExamSession,
        exam: Exam?,
    ):this (
        examSession.id ?: "",
        exam?.let {
            ExamSessionExamSearchResponse(
                it.type,
                it.mode,
                it.name,
                it.description,
            )
        },
        examSession.correctorType,
        examSession.state,
        examSession.enrollDate,
        examSession.startDate,
        examSession.endDate,
        examSession.correctionDate
    )
}

data class ExamSessionExamSearchResponse (
    val type: ExamType,
    val mode: ExamMode,
    val name: String,
    val description: String,
)
