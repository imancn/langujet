package com.cn.speaktest.domain.exam.model

import com.cn.speaktest.domain.student.model.Student
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_request")
data class ExamRequest(
    @Id var id: String?,
    @DBRef var examInfo: ExamInfo,
    @DBRef var student: Student,
    var date: Date,
) {
    constructor(examInfo: ExamInfo, student: Student) : this(
        null, examInfo, student, Date(System.currentTimeMillis())
    )

    constructor(examInfo: ExamInfo, student: Student, date: Date) : this(null, examInfo, student, date)
}