package com.cn.speaktest.actor.question.payload.request

import com.cn.speaktest.domain.answer.model.AnswerType
import jakarta.validation.constraints.NotBlank

open class QuestionRequest(
    var examId: String?,
    @NotBlank var sectionId: String?,
    var topic: String?,
    var order: Int?,
    var usageNumber: Int?,
    var answerType: AnswerType?
)