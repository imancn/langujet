package com.cn.langujet.domain.exam.model

import com.cn.langujet.actor.exam.payload.ExamDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exams")
data class Exam(
    @Id var id: String?,
    var examType: ExamType,
    var name: String,
    var description: String,
    var sectionsNumber: Int,
    var questionNumber: Int,
    var examDuration: Long, // Milliseconds
) {
    constructor(examDto: ExamDto) : this(
        examDto.id,
        examDto.examType,
        examDto.name!!,
        examDto.description!!,
        examDto.sectionsNumber!!,
        examDto.questionNumber!!,
        examDto.examDuration!!
    )
}