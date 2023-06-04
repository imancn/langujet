package com.cn.speaktest.actor.exam.payload.request

import com.cn.speaktest.domain.exam.model.Currency
import com.cn.speaktest.domain.exam.model.Difficulty
import com.cn.speaktest.domain.exam.model.ExamInfo

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
    constructor(examInfo: ExamInfo) : this(
        examInfo.name,
        examInfo.description,
        examInfo.sectionsNumber,
        examInfo.questionNumber,
        examInfo.examDuration,
        examInfo.difficulty,
        examInfo.price.value,
        examInfo.price.currency,
    )
}