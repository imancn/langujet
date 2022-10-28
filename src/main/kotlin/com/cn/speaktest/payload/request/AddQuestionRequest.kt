package com.cn.speaktest.payload.request

import com.cn.speaktest.model.Question

data class AddQuestionRequest(
    val section: Question.Section,
    val topic: String,
    val order: Int,
    val text: String
) {
}