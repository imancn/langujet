package com.cn.speaktest.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exams")
data class ExamInfo(
    @Id var id: String?,
    var name: String,
    var description: String,
    var sectionsNumber: Int,
    var questionNumber: Int,
    var examDuration: Long, // Milliseconds
    var difficulty: Difficulty,
    var price: Price,
)