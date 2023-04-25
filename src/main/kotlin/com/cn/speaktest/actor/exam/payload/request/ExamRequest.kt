package com.cn.speaktest.actor.exam.payload.request

import com.cn.speaktest.domain.exam.model.Currency
import com.cn.speaktest.domain.exam.model.Difficulty
import com.cn.speaktest.domain.exam.model.Exam

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