package com.cn.speaktest.exam.api.request

import com.cn.speaktest.exam.model.*

class ExamRequest(
    val name: String?,
    val description: String?,
    val sectionsNumber: Int?,
    val questionNumber: Int?,
    val examDuration: Long?,
    val difficulty: Difficulty,
    val priceValue: Double?,
    val priceCurrency: Currency?,
) {
    constructor(exam: Exam) : this(
        exam.name,
        exam.description,
        exam.sectionsNumber,
        exam.questionNumber,
        exam.examDuration,
        exam.difficulty,
        exam.price.value,
        exam.price.currency,
    )
}