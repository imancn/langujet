package com.cn.langujet.actor.exam.payload.dto

import com.cn.langujet.domain.exam.model.ExamSection
import jakarta.validation.constraints.NotBlank

data class ExamSectionDto(
    var id: String?,
    @get:NotBlank(message = "Exam session ID is required.")
    var examId: String?,
    @get:NotBlank(message = "Exam ID is required.")
    var examSessionId: String?,
    @get:NotBlank(message = "section ID is required.")
    var sectionId: String?,
    @get:NotBlank(message = "Name is required.")
    var examIssues: List<ExamIssueDto>?,
    var suggestion: SuggestionDto?
) {
    constructor(examSection: ExamSection) : this(
        examSection.id,
        examSection.exam.id,
        examSection.examSessionId,
        examSection.section.id,
        examSection.examIssues.map { ExamIssueDto(it) },
        examSection.suggestion?.let { SuggestionDto(it) }
    )
}