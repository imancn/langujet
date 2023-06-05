package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.ExamSection
import jakarta.validation.constraints.NotBlank

data class ExamSectionDto(
    var id: String?,
    @get:NotBlank(message = "Exam session ID is required.")
    var examSessionId: String?,
    @get:NotBlank(message = "Name is required.")
    var name: String?,
    var examIssues: List<ExamIssueDto>?,
    var suggestion: SuggestionDto?
) {
    constructor(examSection: ExamSection) : this(
        examSection.id,
        examSection.examSessionId,
        examSection.name,
        examSection.examIssues?.map { ExamIssueDto(it) },
        examSection.suggestion?.let { SuggestionDto(it) }
    )
}