package com.cn.langujet.actor.exam.payload.dto

import com.cn.langujet.domain.exam.model.nested.Difficulty
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamType

data class ExamDto(
    val id: String?,
    val examType: ExamType,
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
        exam.examType,
        exam.name,
        exam.description,
        exam.sectionsNumber,
        exam.questionNumber,
        exam.examDuration,
        exam.difficulty,
        PriceDto(exam.price)
    )
}