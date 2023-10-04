package com.cn.langujet.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_request")
data class ExamRequest(
    @Id var id: String?,
    var examType: ExamType,
    var sectionType: SectionType?,
    var studentId: String,
    var date: Date,
) {
    constructor(examType: ExamType, sectionType: SectionType?, student: String) : this(
        null, examType, sectionType, student, Date(System.currentTimeMillis())
    )
}