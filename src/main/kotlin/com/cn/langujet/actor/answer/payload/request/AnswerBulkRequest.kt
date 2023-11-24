package com.cn.langujet.actor.answer.payload.request

import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

sealed class AnswerBulkRequest(
    open val partIndex: Int?,
    open val questionIndex: Int?,
) {
    inline fun <reified T : Answer> convertToAnswer(examSessionId: String, sectionOrder: Int): T {
        val answer: Answer = when (this) {
            is TextBulkAnswerRequest -> Answer.TextAnswer(
                examSessionId,
                sectionOrder,
                partIndex!!,
                questionIndex!!,
                this.text!!
            )

            is TextIssuesBulkAnswerRequest -> Answer.TextIssuesAnswer(
                examSessionId,
                sectionOrder,
                partIndex!!,
                questionIndex!!,
                this.textList!!
            )

            is TrueFalseBulkAnswerRequest -> Answer.TrueFalseAnswer(
                examSessionId,
                sectionOrder,
                partIndex!!,
                questionIndex!!,
                answers!!
            )

            else -> throw IllegalArgumentException("Unsupported answer request type")
        }
        if (answer !is T) {
            throw IllegalArgumentException("The answer type does not match the expected return type.")
        }
        return answer
    }
}

data class TextBulkAnswerRequest(
    @field:NotNull override val partIndex: Int? = null,
    @field:NotNull override val questionIndex: Int? = null,
    @field:NotBlank val text: String? = null,
) : AnswerBulkRequest(partIndex, questionIndex)

data class TextIssuesBulkAnswerRequest(
    @field:NotNull override val partIndex: Int? = null,
    @field:NotNull override val questionIndex: Int? = null,
    @field:NotNull val textList: List<String?>? = null,
) : AnswerBulkRequest(partIndex, questionIndex)

data class TrueFalseBulkAnswerRequest(
    @field:NotNull override val partIndex: Int? = null,
    @field:NotNull override val questionIndex: Int? = null,
    @field:NotNull val answers: List<TrueFalseAnswerType?>? = null,
) : AnswerBulkRequest(partIndex, questionIndex)