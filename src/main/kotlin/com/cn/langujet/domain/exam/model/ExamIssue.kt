package com.cn.langujet.domain.exam.model

import com.cn.langujet.actor.exam.payload.dto.ExamIssueDto
import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.question.model.Question
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exam_issues")
data class ExamIssue(
    @Id var id: String?,
    var examSectionId: String,
    @DBRef var question: Question,
    @DBRef var answer: Answer?,
    var order: Int
) {
    constructor(examSectionId: String, question: Question, order: Int) : this(
        null, examSectionId, question, null, order
    )

    constructor(examIssue: ExamIssueDto) : this(
        examIssue.id,
        examIssue.examSectionId,
        examIssue.question,
        examIssue.answer,
        examIssue.order
    )
}
