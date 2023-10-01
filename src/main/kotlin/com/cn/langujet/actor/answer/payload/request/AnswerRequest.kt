package com.cn.langujet.actor.answer.payload.request

import com.cn.langujet.domain.answer.model.Answer

sealed class AnswerRequest(
    open val examIssueId: String,
    open val examSessionId: String
) {
    inline fun <reified T : Answer> convertToAnswer(userId: String): T {
        val answer: Answer = when (this) {
            is TextAnswerRequest -> Answer.Text(this.examIssueId, userId, this.text)
            is TextIssuesAnswerRequest -> Answer.TextIssues(this.examIssueId, userId, this.text)
            is TrueFalseAnswerRequest -> Answer.TrueFalse(this.examIssueId, userId, this.isTrue)
            is VoiceAnswerRequest -> Answer.Voice(this.examIssueId, userId, this.audioAddress)
            else -> throw IllegalArgumentException("Unsupported answer request type")
        }
        if (answer !is T) {
            throw IllegalArgumentException("The answer type does not match the expected return type.")
        }
        return answer
    }
}

data class TextAnswerRequest(
    override val examIssueId: String,
    override val examSessionId: String,
    val text: String
) : AnswerRequest(examIssueId, examSessionId)

data class TextIssuesAnswerRequest(
    override val examIssueId: String,
    override val examSessionId: String,
    val text: List<String>
) : AnswerRequest(examIssueId, examSessionId)

data class TrueFalseAnswerRequest(
    override val examIssueId: String,
    override val examSessionId: String,
    val isTrue: Boolean?
) : AnswerRequest(examIssueId, examSessionId)

data class VoiceAnswerRequest(
    override val examIssueId: String,
    override val examSessionId: String,
    val audioAddress: String
) : AnswerRequest(examIssueId, examSessionId)
