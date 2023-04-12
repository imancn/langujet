package com.cn.speaktest.question.api.request

import com.cn.speaktest.answer.model.AnswerType

open class QuestionRequest(
    var examId: String?,
    var topic: String?,
    var section: Int?,
    var order: Int?,
    var usageNumber: Int?,
    var answerType: AnswerType?
)