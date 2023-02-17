package com.cn.speaktest.exam.model

import com.cn.speaktest.professor.Professor
import com.cn.speaktest.student.model.Student
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
    var examIssues: List<ExamIssue>?,
    var score: Double?,
    var rank: Double?,
    @DBRef
    var suggestion: Suggestion?,
    var startDate: Date?,
    var requestDate: Date?,
) {
    constructor(student: Student, professor: Professor, requestDate: Date): this(
        null,
        student,
        professor,
        null,
        null,
        null,
        null,
        null,
        requestDate
    )
}