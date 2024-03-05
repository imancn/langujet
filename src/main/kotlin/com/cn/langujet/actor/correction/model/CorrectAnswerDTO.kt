package com.cn.langujet.actor.correction.model

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import com.cn.langujet.domain.correction.model.CorrectAnswer
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(
    subTypes = [
        CorrectTextAnswerDTO::class,
        CorrectTextIssuesAnswerDTO::class,
        CorrectTrueFalseAnswerDTO::class,
        CorrectMultipleChoiceAnswerDTO::class
    ]
)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = CorrectTextAnswerDTO::class, name = "TEXT"),
    JsonSubTypes.Type(value = CorrectTextIssuesAnswerDTO::class, name = "TEXT_ISSUES"),
    JsonSubTypes.Type(value = CorrectTrueFalseAnswerDTO::class, name = "TRUE_FALSE"),
    JsonSubTypes.Type(value = CorrectMultipleChoiceAnswerDTO::class, name = "MULTIPLE_CHOICE")
)
sealed class CorrectAnswerDTO(
    open val id: String?,
    open val partOrder: Int?,
    open val questionOrder: Int?,
    val type: AnswerType,
) {
    inline fun <reified T : CorrectAnswer> toCorrectAnswer(examId: String, sectionOrder: Int): T {
        val answer: CorrectAnswer = when (this) {
            is CorrectTextAnswerDTO -> CorrectAnswer.CorrectTextAnswer(
                examId,
                sectionOrder,
                partOrder!!,
                questionOrder!!,
                this.text!!
            )

            is CorrectTextIssuesAnswerDTO -> CorrectAnswer.CorrectTextIssuesAnswer(
                examId,
                sectionOrder,
                partOrder!!,
                questionOrder!!,
                this.textList!!
            )

            is CorrectTrueFalseAnswerDTO -> CorrectAnswer.CorrectTrueFalseAnswer(
                examId,
                sectionOrder,
                partOrder!!,
                questionOrder!!,
                this.answers!!
            )

            is CorrectMultipleChoiceAnswerDTO -> CorrectAnswer.CorrectMultipleChoiceAnswer(
                examId,
                sectionOrder,
                partOrder!!,
                questionOrder!!,
                this.issues!!.map { it.toMultipleChoiceIssueAnswer() }
            )

            else -> throw IllegalArgumentException("Unsupported answer DTO type")
        }
        if (answer !is T) {
            throw IllegalArgumentException("The answer type does not match the expected return type.")
        }
        return answer
    }

    companion object {
        inline fun <reified T : CorrectAnswerDTO> fromCorrectAnswer(correctAnswer: CorrectAnswer): T {
            val correctAnswerDTO = when (correctAnswer) {
                is CorrectAnswer.CorrectTextAnswer -> CorrectTextAnswerDTO(correctAnswer)
                is CorrectAnswer.CorrectTextIssuesAnswer -> CorrectTextIssuesAnswerDTO(correctAnswer)
                is CorrectAnswer.CorrectTrueFalseAnswer -> CorrectTrueFalseAnswerDTO(correctAnswer)
                is CorrectAnswer.CorrectMultipleChoiceAnswer -> CorrectMultipleChoiceAnswerDTO(correctAnswer)
            }
            if (correctAnswerDTO !is T) throw TypeCastException("The type of correct answer does not match the reified type.")
            return correctAnswerDTO

        }
    }
}

data class CorrectTextAnswerDTO(
    override val id: String? = null,
    @field:NotNull override val partOrder: Int? = null,
    @field:NotNull override val questionOrder: Int? = null,
    @field:NotBlank val text: String? = null,
) : CorrectAnswerDTO(id, partOrder, questionOrder, AnswerType.TEXT) {
    constructor(answer: CorrectAnswer.CorrectTextAnswer) : this(
        id = answer.id,
        partOrder = answer.partOrder,
        questionOrder = answer.questionOrder,
        text = answer.text
    )
}

data class CorrectTextIssuesAnswerDTO(
    override val id: String? = null,
    @field:NotNull override val partOrder: Int? = null,
    @field:NotNull override val questionOrder: Int? = null,
    @field:NotNull val textList: List<String>? = null,
) : CorrectAnswerDTO(id, partOrder, questionOrder, AnswerType.TEXT_ISSUES) {
    constructor(answer: CorrectAnswer.CorrectTextIssuesAnswer) : this(
        id = answer.id,
        partOrder = answer.partOrder,
        questionOrder = answer.questionOrder,
        textList = answer.textList
    )
}

data class CorrectTrueFalseAnswerDTO(
    override val id: String? = null,
    @field:NotNull override val partOrder: Int? = null,
    @field:NotNull override val questionOrder: Int? = null,
    @field:NotNull val answers: List<TrueFalseAnswerType>? = null,
) : CorrectAnswerDTO(id, partOrder, questionOrder, AnswerType.TRUE_FALSE) {
    constructor(answer: CorrectAnswer.CorrectTrueFalseAnswer) : this(
        id = answer.id,
        partOrder = answer.partOrder,
        questionOrder = answer.questionOrder,
        answers = answer.answers
    )
}

data class CorrectMultipleChoiceAnswerDTO(
    override val id: String? = null,
    @field:NotNull override val partOrder: Int? = null,
    @field:NotNull override val questionOrder: Int? = null,
    @field:NotNull val issues: List<CorrectMultipleChoiceIssueAnswerDTO>? = null,
) : CorrectAnswerDTO(id, partOrder, questionOrder, AnswerType.MULTIPLE_CHOICE) {
    constructor(answer: CorrectAnswer.CorrectMultipleChoiceAnswer) : this(
        id = answer.id,
        partOrder = answer.partOrder,
        questionOrder = answer.questionOrder,
        issues = answer.issues.map { issue -> CorrectMultipleChoiceIssueAnswerDTO(issue) }
    )
}

class CorrectMultipleChoiceIssueAnswerDTO(
    @field:NotNull var issueOrder: Int? = null,
    @field:NotNull var options: List<String>? = null
) {
    constructor(issue: CorrectAnswer.CorrectMultipleChoiceIssueAnswer) : this(
        issue.order, issue.options
    )

    fun toMultipleChoiceIssueAnswer(): CorrectAnswer.CorrectMultipleChoiceIssueAnswer {
        return CorrectAnswer.CorrectMultipleChoiceIssueAnswer(
            this.issueOrder!!,
            this.options!!
        )
    }
}