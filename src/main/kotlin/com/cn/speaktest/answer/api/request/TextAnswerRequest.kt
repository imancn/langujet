package com.cn.speaktest.answer.api.request

data class TextAnswerRequest(
    val examIssueId: String,
    val userId: String,
    val text: String
)