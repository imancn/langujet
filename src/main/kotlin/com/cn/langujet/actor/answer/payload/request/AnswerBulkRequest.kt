package com.cn.langujet.actor.answer.payload.request

import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
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
    open val partOrder: Int?,
    open val questionOrder: Int?,
    val answerType: AnswerType?
) {
    inline fun <reified T : Answer> convertToAnswer(examSessionId: String, sectionOrder: Int): T {
        val answer: Answer = when (this) {
            is TextBulkAnswerRequest -> Answer.TextAnswer(
                examSessionId,
                sectionOrder,
                partOrder!!,
                questionOrder!!,
                Date(System.currentTimeMillis()),
                this.text!!
            )

            is TextIssuesBulkAnswerRequest -> Answer.TextIssuesAnswer(
                examSessionId,
                sectionOrder,
                partOrder!!,
                questionOrder!!,
                Date(System.currentTimeMillis()),
                this.issues!!
            )

            is TrueFalseBulkAnswerRequest -> Answer.TrueFalseAnswer(
                examSessionId,
                sectionOrder,
                partOrder!!,
                questionOrder!!,
                Date(System.currentTimeMillis()),
                this.issues!!
            )

            is MultipleChoiceBulkAnswerRequest -> Answer.MultipleChoiceAnswer(
                examSessionId,
                sectionOrder,
                partOrder!!,
                questionOrder!!,
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
    @field:NotNull override val partOrder: Int? = null,
    @field:NotNull override val questionOrder: Int? = null,
    @field:NotNull val text: String? = null,
) : AnswerBulkRequest(partOrder, questionOrder, AnswerType.TEXT)

data class TextIssuesBulkAnswerRequest(
    @field:NotNull override val partOrder: Int? = null,
    @field:NotNull override val questionOrder: Int? = null,
    @field:NotNull val issues: List<String?>? = null,
) : AnswerBulkRequest(partOrder, questionOrder, AnswerType.TEXT_ISSUES)

data class TrueFalseBulkAnswerRequest(
    @field:NotNull override val partOrder: Int? = null,
    @field:NotNull override val questionOrder: Int? = null,
    @field:NotNull val issues: List<TrueFalseAnswerType?>? = null,
) : AnswerBulkRequest(partOrder, questionOrder, AnswerType.TRUE_FALSE)

data class MultipleChoiceBulkAnswerRequest(
    @field:NotNull override val partOrder: Int? = null,
    @field:NotNull override val questionOrder: Int? = null,
    @field:NotNull val issues: List<MultipleChoiceIssueAnswerRequest?>? = null,
) : AnswerBulkRequest(partOrder, questionOrder, AnswerType.MULTIPLE_CHOICE)