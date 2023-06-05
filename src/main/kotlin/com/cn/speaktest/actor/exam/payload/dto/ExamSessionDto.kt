package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.ExamSession
import java.util.*

data class ExamSessionDto(
    val id: String?,
    val examInfo: ExamMetaDto,
    val studentId: String?,
    val professorId: String?,
    val examSections: List<ExamSectionDto>?,
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
        ExamMetaDto(examSession.examMeta),
        examSession.student.id,
        examSession.professor.id,
        examSession.examSections?.map { ExamSectionDto(it) },
        examSession.requestDate,
        examSession.startDate,
        examSession.endDate,
        examSession.rateDate,
        examSession.isStarted,
        examSession.isFinished,
        examSession.isRated,
    )
}