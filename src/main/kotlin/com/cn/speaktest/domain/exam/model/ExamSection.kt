package com.cn.speaktest.domain.exam.model

import com.cn.speaktest.actor.exam.payload.dto.ExamSectionDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class ExamSection(
    @Id var id: String?,
    var examSessionId: String,
    @DBRef var exam: Exam,
    @DBRef var section: Section,
    @DBRef var examIssues: List<ExamIssue>,
    @DBRef var suggestion: Suggestion?,
) {
    constructor(examSection: ExamSectionDto, exam: Exam, section: Section) : this(
        examSection.id,
        examSection.examSessionId!!,
        exam,
        section,
        examSection.examIssues?.map { ExamIssue(it) }!!,
        examSection.suggestion?.let { Suggestion(it) }
    )

    constructor(
        examSessionId: String,
        exam: Exam,
        section: Section,
        examIssues: List<ExamIssue>
    ) : this(
        null,
        examSessionId,
        exam,
        section,
        examIssues,
        null
    )
}