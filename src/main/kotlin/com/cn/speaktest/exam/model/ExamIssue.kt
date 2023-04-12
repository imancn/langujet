package com.cn.speaktest.exam.model

import com.cn.speaktest.answer.model.Answer
import com.cn.speaktest.question.model.Question
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exam_issues")
data class ExamIssue(
    @Id
    var id: String?,
    var examSessionId: String,
    @DBRef
    var question: Question,
    @DBRef
    var answer: Answer?,
    var order: Int
) {
    constructor(examId: String, question: Question, order: Int): this(
        null, examId, question, null, order
    )
}
