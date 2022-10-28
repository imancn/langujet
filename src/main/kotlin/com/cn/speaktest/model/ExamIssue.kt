package com.cn.speaktest.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exam_issues")
data class ExamIssue(
    @Id
    var id: String?,
    var examId: String,
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
