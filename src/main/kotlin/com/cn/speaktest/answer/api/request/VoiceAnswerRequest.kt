package com.cn.speaktest.answer.api.request

data class VoiceAnswerRequest(
    val examIssueId: String,
    val userId: String,
    val audioUrl: String
)