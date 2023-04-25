package com.cn.speaktest.actor.question.payload.request

import com.cn.speaktest.domain.answer.model.AnswerType

open class QuestionRequest(
    var examId: String?,
    var topic: String?,
    var section: Int?,
    var order: Int?,
    var usageNumber: Int?,
    var answerType: AnswerType?
)