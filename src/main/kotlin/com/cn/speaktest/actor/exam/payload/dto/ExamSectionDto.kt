package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.ExamSection

data class ExamSectionDto(
    val id: String,
    val examSessionId: String,
    val name: String,
    val examIssues: List<ExamIssueDto>?,
    val suggestion: SuggestionDto?,
) {
    constructor(examSection: ExamSection) : this(
        examSection.id,
        examSection.examSessionId,
        examSection.name,
        examSection.examIssues?.map { ExamIssueDto(it) },
        examSection.suggestion?.let { SuggestionDto(it) }
    )
}