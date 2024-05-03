package com.cn.langujet.actor.answer.payload.request

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.validation.constraints.NotNull

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
)

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

data class MultipleChoiceIssueAnswerRequest(
    @field:NotNull var issueOrder: Int? = null,
    @field:NotNull var options: List<String?>? = null
)