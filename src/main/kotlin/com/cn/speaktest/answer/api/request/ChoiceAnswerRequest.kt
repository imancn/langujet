package com.cn.speaktest.answer.api.request

data class ChoiceAnswerRequest(
    val examIssueId: String,
    val userId: String,
    val choice: String
)