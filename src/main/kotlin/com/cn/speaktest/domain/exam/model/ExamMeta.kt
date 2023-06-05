package com.cn.speaktest.domain.exam.model

import com.cn.speaktest.actor.exam.payload.dto.ExamMetaDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exam_metas")
data class ExamMeta(
    @Id var id: String?,
    var name: String,
    var description: String,
    var sectionsNumber: Int,
    var questionNumber: Int,
    var examDuration: Long, // Milliseconds
    var difficulty: Difficulty,
    var price: Price,
) {
    constructor(examInfo: ExamMetaDto) : this(
        examInfo.id,
        examInfo.name,
        examInfo.description,
        examInfo.sectionsNumber,
        examInfo.questionNumber,
        examInfo.examDuration,
        examInfo.difficulty,
        Price(examInfo.price)
    )
}