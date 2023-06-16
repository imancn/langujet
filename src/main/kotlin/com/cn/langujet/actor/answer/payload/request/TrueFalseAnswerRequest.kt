package com.cn.langujet.actor.answer.payload.request

data class TrueFalseAnswerRequest(
    val examIssueId: String,
    val userId: String,
    val isTrue: Boolean
)