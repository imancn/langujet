package com.cn.langujet.actor.exam.payload.dto

import com.cn.langujet.domain.exam.model.ExamSession
import java.util.*

data class ExamSessionDto(
    val id: String?,
    val examId: String?,
    val studentId: String?,
    val professorId: String?,
    val examSectionIdList: List<String>?,
    val requestDate: Date,
    val startDate: Date?,
    val endDate: Date?,
    val rateDate: Date?,
    val isStarted: Boolean = false,
    val isFinished: Boolean = false,
    val isRated: Boolean = false,
) {
    constructor(examSession: ExamSession) : this(
        examSession.id,
        examSession.exam.id,
        examSession.student.id,
        examSession.professor.id,
        examSession.examSections?.mapNotNull { it.id },
        examSession.requestDate,
        examSession.startDate,
        examSession.endDate,
        examSession.rateDate,
        examSession.isStarted,
        examSession.isFinished,
        examSession.isRated,
    )
}