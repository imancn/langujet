package com.cn.langujet.actor.answer.payload.request

import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

sealed class AnswerRequest(
        open val examSessionId: String?,
        open val sectionOrder: Int?,
        open val partId: Int?,
        open val questionId: Int?,
) {
    inline fun <reified T : Answer> convertToAnswer(): T {
        val answer: Answer = when (this) {
            is TextAnswerRequest -> Answer.TextAnswer(
                examSessionId!!,
                sectionOrder!!,
                partId!!,
                questionId!!,
                Date(System.currentTimeMillis()),
                this.text!!
            )

            is TextIssuesAnswerRequest -> Answer.TextIssuesAnswer(
                examSessionId!!,
                sectionOrder!!,
                partId!!,
                questionId!!,
                Date(System.currentTimeMillis()),
                this.textList!!
            )

            is TrueFalseAnswerRequest -> Answer.TrueFalseAnswer(
                examSessionId!!,
                sectionOrder!!,
                partId!!,
                questionId!!,
                Date(System.currentTimeMillis()),
                this.answers!!
            )

            is MultipleChoiceAnswerRequest -> Answer.MultipleChoiceAnswer(
                examSessionId!!,
                sectionOrder!!,
                partId!!,
                questionId!!,
                Date(System.currentTimeMillis()),
                this.issues!!.mapNotNull { it?.toMultipleChoiceIssueAnswer() }
            )

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
        @field:NotNull override val partId: Int? = null,
        @field:NotNull override val questionId: Int? = null,
        @field:NotBlank val text: String? = null,
) : AnswerRequest(examSessionId, sectionOrder, partId, questionId)

data class TextIssuesAnswerRequest(
        @field:NotBlank override val examSessionId: String? = null,
        @field:NotNull override val sectionOrder: Int? = null,
        @field:NotNull override val partId: Int? = null,
        @field:NotNull override val questionId: Int? = null,
        @field:NotNull val textList: List<String?>? = null,
) : AnswerRequest(examSessionId, sectionOrder, partId, questionId)

data class TrueFalseAnswerRequest(
        @field:NotBlank override val examSessionId: String? = null,
        @field:NotNull override val sectionOrder: Int? = null,
        @field:NotNull override val partId: Int? = null,
        @field:NotNull override val questionId: Int? = null,
        @field:NotNull val answers: List<TrueFalseAnswerType?>? = null,
) : AnswerRequest(examSessionId, sectionOrder, partId, questionId)

data class MultipleChoiceAnswerRequest(
        @field:NotBlank override val examSessionId: String? = null,
        @field:NotNull override val sectionOrder: Int? = null,
        @field:NotNull override val partId: Int? = null,
        @field:NotNull override val questionId: Int? = null,
        @field:NotNull val issues: List<MultipleChoiceIssueAnswerRequest?>? = null,
) : AnswerRequest(examSessionId, sectionOrder, partId, questionId)