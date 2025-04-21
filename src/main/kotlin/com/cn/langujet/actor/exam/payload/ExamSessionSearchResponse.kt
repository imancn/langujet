package com.cn.langujet.actor.exam.payload

import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamEntity
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamSessionState
import com.cn.langujet.domain.exam.model.enums.ExamType
import java.util.*

class ExamSessionSearchResponse (
    val examSessionId: Long,
    val exam: ExamSessionExamSearchResponse?,
    val correctorType: CorrectorType?,
    val state: ExamSessionState,
    val enrollDate: Date,
    val startDate: Date?,
    val endDate: Date?,
    val correctionDate: Date?
) {
    constructor(
        examSession: ExamSessionEntity,
        exam: ExamEntity?,
    ):this (
        examSession.id ?: Entity.UNKNOWN_ID,
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
