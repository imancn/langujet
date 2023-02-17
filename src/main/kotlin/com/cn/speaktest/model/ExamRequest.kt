package com.cn.speaktest.model

import com.cn.speaktest.student.model.Student
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_request")
data class ExamRequest(
    @Id
    var id: String?,
    var date: Date,
    @DBRef
    var student: Student,
) {
    constructor(date: Date, student: Student): this(
        null, date, student
    )
}
