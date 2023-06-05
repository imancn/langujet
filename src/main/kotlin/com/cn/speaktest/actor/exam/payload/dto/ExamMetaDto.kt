package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.Difficulty
import com.cn.speaktest.domain.exam.model.ExamMeta

data class ExamMetaDto(
    val id: String?,
    val name: String,
    val description: String,
    val sectionsNumber: Int,
    val questionNumber: Int,
    val examDuration: Long, // Milliseconds
    val difficulty: Difficulty,
    val price: PriceDto,
) {
    constructor(examMeta: ExamMeta) : this(
        examMeta.id,
        examMeta.name,
        examMeta.description,
        examMeta.sectionsNumber,
        examMeta.questionNumber,
        examMeta.examDuration,
        examMeta.difficulty,
        PriceDto(examMeta.price)
    )
}