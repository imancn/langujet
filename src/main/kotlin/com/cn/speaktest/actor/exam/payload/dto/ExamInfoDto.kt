package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.Difficulty
import com.cn.speaktest.domain.exam.model.ExamInfo

data class ExamInfoDto(
    val id: String?,
    val name: String,
    val description: String,
    val sectionsNumber: Int,
    val questionNumber: Int,
    val examDuration: Long, // Milliseconds
    val difficulty: Difficulty,
    val price: PriceDto,
) {
    constructor(examInfo: ExamInfo) : this(
        examInfo.id,
        examInfo.name,
        examInfo.description,
        examInfo.sectionsNumber,
        examInfo.questionNumber,
        examInfo.examDuration,
        examInfo.difficulty,
        PriceDto(examInfo.price)
    )
}