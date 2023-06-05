package com.cn.speaktest.domain.exam.model

import com.cn.speaktest.actor.exam.payload.dto.ExamSectionDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class ExamSection(
    @Id var id: String?,
    var examSessionId: String,
    var name: String,
    val order: Int,
    @DBRef var examIssues: List<ExamIssue>?,
    @DBRef var suggestion: Suggestion?,
) {
    constructor(examSection: ExamSectionDto) : this(
        examSection.id,
        examSection.examSessionId!!,
        examSection.name!!,
        examSection.order,
        examSection.examIssues?.map { ExamIssue(it) },
        examSection.suggestion?.let { Suggestion(it) }
    )
}