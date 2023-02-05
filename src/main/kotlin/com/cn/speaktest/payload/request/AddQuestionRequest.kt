package com.cn.speaktest.payload.request

import com.cn.speaktest.model.Question
import javax.validation.constraints.NotBlank

data class AddQuestionRequest(
    @field:NotBlank val section: Question.Section,
    @field:NotBlank val topic: String,
    val order: Int,
    @field:NotBlank val text: String
) {
}