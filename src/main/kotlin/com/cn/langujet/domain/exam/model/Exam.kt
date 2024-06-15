package com.cn.langujet.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exams")
data class Exam(
    @Id var id: String?,
    var type: ExamType,
    var mode: ExamMode,
    var name: String,
    var description: String,
    var sectionsNumber: Int,
    var questionNumber: Int,
    var examDuration: Long, // Seconds
    var active: Boolean
)