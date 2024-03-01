package com.cn.langujet.actor.answer.payload.request

import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "answerType"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = TextBulkAnswerRequest::class, name = "TEXT"),
    JsonSubTypes.Type(value = TextIssuesBulkAnswerRequest::class, name = "TEXT_ISSUES"),
    JsonSubTypes.Type(value = TrueFalseBulkAnswerRequest::class, name = "TRUE_FALSE"),
    JsonSubTypes.Type(value = MultipleChoiceBulkAnswerRequest::class, name = "MULTIPLE_CHOICE"),
)
sealed class AnswerBulkRequest(
    open val partId: Int?,
    open val questionId: Int?,
    val answerType: AnswerType?
) {
    inline fun <reified T : Answer> convertToAnswer(examSessionId: String, sectionOrder: Int): T {
        val answer: Answer = when (this) {
            is TextBulkAnswerRequest -> Answer.TextAnswer(
                examSessionId,
                sectionOrder,
                partId!!,
                questionId!!,
                Date(System.currentTimeMillis()),
                this.text!!
            )

            is TextIssuesBulkAnswerRequest -> Answer.TextIssuesAnswer(
                examSessionId,
                sectionOrder,
                partId!!,
                questionId!!,
                Date(System.currentTimeMillis()),
                this.textList!!
            )

            is TrueFalseBulkAnswerRequest -> Answer.TrueFalseAnswer(
                examSessionId,
                sectionOrder,
                partId!!,
                questionId!!,
                Date(System.currentTimeMillis()),
                this.answers!!
            )

            is MultipleChoiceBulkAnswerRequest -> Answer.MultipleChoiceAnswer(
                examSessionId,
                sectionOrder,
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

data class TextBulkAnswerRequest(
    @field:NotNull override val partId: Int? = null,
    @field:NotNull override val questionId: Int? = null,
    @field:NotBlank val text: String? = null,
) : AnswerBulkRequest(partId, questionId, AnswerType.TEXT)

data class TextIssuesBulkAnswerRequest(
    @field:NotNull override val partId: Int? = null,
    @field:NotNull override val questionId: Int? = null,
    @field:NotNull val textList: List<String?>? = null,
) : AnswerBulkRequest(partId, questionId, AnswerType.TEXT_ISSUES)

data class TrueFalseBulkAnswerRequest(
    @field:NotNull override val partId: Int? = null,
    @field:NotNull override val questionId: Int? = null,
    @field:NotNull val answers: List<TrueFalseAnswerType?>? = null,
) : AnswerBulkRequest(partId, questionId, AnswerType.TRUE_FALSE)

data class MultipleChoiceBulkAnswerRequest(
    @field:NotNull override val partId: Int? = null,
    @field:NotNull override val questionId: Int? = null,
    @field:NotNull val issues: List<MultipleChoiceIssueAnswerRequest?>? = null,
) : AnswerBulkRequest(partId, questionId, AnswerType.MULTIPLE_CHOICE)