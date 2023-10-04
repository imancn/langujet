package com.cn.langujet.domain.exam.model

import com.cn.langujet.actor.exam.payload.dto.ExamDto
import com.cn.langujet.domain.exam.model.nested.Difficulty
import com.cn.langujet.domain.exam.model.nested.Price
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
    var difficulty: Difficulty,
    var price: Price,
) {
    constructor(examDto: ExamDto) : this(
        examDto.id,
        examDto.examType,
        examDto.name!!,
        examDto.description!!,
        examDto.sectionsNumber!!,
        examDto.questionNumber!!,
        examDto.examDuration!!,
        examDto.difficulty!!,
        Price(examDto.price!!)
    )
}