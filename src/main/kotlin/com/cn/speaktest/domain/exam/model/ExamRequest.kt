package com.cn.speaktest.domain.exam.model

import com.cn.speaktest.domain.student.model.Student
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_request")
data class ExamRequest(
    @Id var id: String?,
    @DBRef var exam: Exam,
    @DBRef var student: Student,
    var date: Date,
) {
    constructor(exam: Exam, student: Student) : this(
        null, exam, student, Date(System.currentTimeMillis())
    )

    constructor(exam: Exam, student: Student, date: Date) : this(null, exam, student, date)
}