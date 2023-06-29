package com.cn.langujet.actor.exam.payload.dto

import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.exam.model.ExamIssue
import com.cn.langujet.domain.question.model.Question

data class ExamIssueDto(
    val id: String?,
    val examSectionId: String,
    val question: Question,
    val answer: Answer?,
    val order: Int
) {

    constructor(examIssue: ExamIssue) : this(
        examIssue.id,
        examIssue.examSectionId,
        examIssue.question,
        examIssue.answer,
        examIssue.order
    )
}
