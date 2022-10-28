package com.cn.speaktest.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exams")
data class Exam(
    @Id
    var id: String?,
    @DBRef
    var student: Student,
    @DBRef
    var professor: Professor,
    @DBRef
    val examIssues: List<ExamIssue>?,
    var score: Double?,
    var rank: Double?,
    @DBRef
    var suggestion: Suggestion?,
    var requestDate: Date?,
    var startDate: Date?
)