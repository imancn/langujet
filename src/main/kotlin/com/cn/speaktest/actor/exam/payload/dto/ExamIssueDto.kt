package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.answer.model.Answer
import com.cn.speaktest.domain.exam.model.ExamIssue
import com.cn.speaktest.domain.question.model.Question

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
