package com.cn.langujet.actor.answer.payload.request

import com.cn.langujet.domain.answer.model.Answer
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

sealed class AnswerRequest(
    open val examSessionId: String?,
    open val sectionOrder: Int?,
    open val partIndex: Int?,
    open val questionIndex: Int?,
) {
    inline fun <reified T : Answer> convertToAnswer(): T {
        val answer: Answer = when (this) {
            is TextAnswerRequest -> Answer.Text(examSessionId!!, sectionOrder!!, partIndex!!, questionIndex!!, this.text!!)
            is TextIssuesAnswerRequest -> Answer.TextIssues(examSessionId!!, sectionOrder!!, partIndex!!, questionIndex!!, this.textList!!)
            is TrueFalseAnswerRequest -> Answer.TrueFalse(examSessionId!!, sectionOrder!!, partIndex!!, questionIndex!!, booleanList!!)
            is VoiceAnswerRequest -> Answer.Voice(examSessionId!!, sectionOrder!!, partIndex!!, questionIndex!!, this.audioId!!)
            else -> throw IllegalArgumentException("Unsupported answer request type")
        }
        if (answer !is T) {
            throw IllegalArgumentException("The answer type does not match the expected return type.")
        }
        return answer
    }
}

data class TextAnswerRequest(
    @field:NotBlank override val examSessionId: String? = null,
    @field:NotNull override val sectionOrder: Int? = null,
    @field:NotNull override val partIndex: Int? = null,
    @field:NotNull override val questionIndex: Int? = null,
    @field:NotBlank val text: String? = null,
) : AnswerRequest(examSessionId, sectionOrder, partIndex, questionIndex)

data class TextIssuesAnswerRequest(
    @field:NotBlank override val examSessionId: String? = null,
    @field:NotNull override val sectionOrder: Int? = null,
    @field:NotNull override val partIndex: Int? = null,
    @field:NotNull override val questionIndex: Int? = null,
    @field:NotNull val textList: List<String?>? = null,
) : AnswerRequest(examSessionId, sectionOrder, partIndex, questionIndex)

data class TrueFalseAnswerRequest(
    @field:NotBlank override val examSessionId: String? = null,
    @field:NotNull override val sectionOrder: Int? = null,
    @field:NotNull override val partIndex: Int? = null,
    @field:NotNull override val questionIndex: Int? = null,
    @field:NotNull val booleanList: List<Boolean?>? = null,
) : AnswerRequest(examSessionId, sectionOrder, partIndex, questionIndex)

data class VoiceAnswerRequest(
    @field:NotBlank override val examSessionId: String? = null,
    @field:NotNull override val sectionOrder: Int? = null,
    @field:NotNull override val partIndex: Int? = null,
    @field:NotNull override val questionIndex: Int? = null,
    @field:NotBlank val audioId: String? = null,
) : AnswerRequest(examSessionId, sectionOrder, partIndex, questionIndex)
