package com.cn.langujet.actor.answer.payload.request

data class TextAnswerRequest(
    val examIssueId: String,
    val userId: String,
    val text: String
)