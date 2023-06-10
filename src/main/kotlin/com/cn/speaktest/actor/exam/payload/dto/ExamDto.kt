package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.nested.Difficulty
import com.cn.speaktest.domain.exam.model.Exam

data class ExamDto(
    val id: String?,
    val sections: List<SectionDto>?,
    val name: String?,
    val description: String?,
    val sectionsNumber: Int?,
    val questionNumber: Int?,
    val examDuration: Long?, // Milliseconds
    val difficulty: Difficulty?,
    val price: PriceDto?,
) {

    constructor(exam: Exam) : this(
        exam.id,
        exam.sections.map { SectionDto(it) },
        exam.name,
        exam.description,
        exam.sectionsNumber,
        exam.questionNumber,
        exam.examDuration,
        exam.difficulty,
        PriceDto(exam.price)
    )
}