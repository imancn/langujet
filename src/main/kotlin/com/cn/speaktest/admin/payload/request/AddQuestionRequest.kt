package com.cn.speaktest.admin.payload.request

import com.cn.speaktest.exam.model.Question
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AddQuestionRequest(
    @field:NotNull val section: Question.Section?,
    @field:NotBlank val topic: String?,
    @field:NotNull val order: Int?,
    @field:NotBlank val text: String?
) {
}