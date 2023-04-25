package com.cn.speaktest.actor.answer.payload.request

data class VoiceAnswerRequest(
    val examIssueId: String,
    val userId: String,
    val audioUrl: String
)