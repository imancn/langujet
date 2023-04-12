package com.cn.speaktest.answer.api.request

data class TrueFalseAnswerRequest(
    val examIssueId: String,
    val userId: String,
    val isTrue: Boolean
)