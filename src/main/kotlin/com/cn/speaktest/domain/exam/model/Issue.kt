package com.cn.speaktest.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "issues")
data class Issue(
    @Id
    var id: String?,

    @DBRef
    var exam: Exam,
    @DBRef
    var examIssue: ExamIssue,

    var content: String,
    var improvement: String
)