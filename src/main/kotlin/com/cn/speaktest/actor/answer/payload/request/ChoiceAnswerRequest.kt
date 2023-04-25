package com.cn.speaktest.actor.answer.payload.request

data class ChoiceAnswerRequest(
    val examIssueId: String,
    val userId: String,
    val choice: String
)