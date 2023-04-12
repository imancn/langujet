package com.cn.speaktest.exam.model

import com.cn.speaktest.student.model.Student
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_request")
data class ExamRequest(
    @Id var id: String?,
    var examId: String,
    var date: Date,
    @DBRef var student: Student,
) {
    constructor(examId: String, student: Student) : this(
        null, examId, Date(System.currentTimeMillis()), student
    )
}
