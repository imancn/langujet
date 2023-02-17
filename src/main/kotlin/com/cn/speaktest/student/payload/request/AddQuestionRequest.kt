package com.cn.speaktest.student.payload.request

import com.cn.speaktest.exam.model.Question
import jakarta.validation.constraints.NotBlank

data class AddQuestionRequest(
    @field:NotBlank val section: Question.Section,
    @field:NotBlank val topic: String,
    val order: Int,
    @field:NotBlank val text: String
) {
}