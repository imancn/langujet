package com.cn.langujet.actor.answer.payload.request

import com.cn.langujet.domain.answer.model.Answer

sealed class AnswerRequest(
    open val examIssueId: String,
    open val examSessionId: String,
    open val sectionId: String,
    open val partIndex: Int,
    open val questionIndex: Int
) {
    inline fun <reified T : Answer> convertToAnswer(): T {
        val answer: Answer = when (this) {
            is TextAnswerRequest -> Answer.Text(examSessionId, sectionId, partIndex, questionIndex, this.text)
            is TextIssuesAnswerRequest -> Answer.TextIssues(examSessionId, sectionId, partIndex, questionIndex, this.textList)
            is TrueFalseAnswerRequest -> Answer.TrueFalse(examSessionId, sectionId, partIndex, questionIndex, booleanList)
            is VoiceAnswerRequest -> Answer.Voice(examSessionId, sectionId, partIndex, questionIndex, this.audioAddress)
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
    override val sectionId: String,
    override val partIndex: Int,
    override val questionIndex: Int,
    val text: String
) : AnswerRequest(examIssueId, examSessionId, sectionId, partIndex, questionIndex)

data class TextIssuesAnswerRequest(
    override val examIssueId: String,
    override val examSessionId: String,
    override val sectionId: String,
    override val partIndex: Int,
    override val questionIndex: Int,
    val textList: List<String>
) : AnswerRequest(examIssueId, examSessionId, sectionId, partIndex, questionIndex)

data class TrueFalseAnswerRequest(
    override val examIssueId: String,
    override val examSessionId: String,
    override val sectionId: String,
    override val partIndex: Int,
    override val questionIndex: Int,
    val booleanList: List<Boolean?>
) : AnswerRequest(examIssueId, examSessionId, sectionId, partIndex, questionIndex)

data class VoiceAnswerRequest(
    override val examIssueId: String,
    override val examSessionId: String,
    override val sectionId: String,
    override val partIndex: Int,
    override val questionIndex: Int,
    val audioAddress: String
) : AnswerRequest(examIssueId, examSessionId, sectionId, partIndex, questionIndex)
