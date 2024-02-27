package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.question.*
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    subTypes = [
        SpeakingQuestionDTO::class,
        WritingQuestionDTO::class,
        ReadingTextCompletionDTO::class,
        ReadingTableCompletionDTO::class,
        ReadingMultipleChoiceDTO::class,
        ReadingMatchingFeaturesDTO::class,
        ReadingMatchingEndingsDTO::class,
        ReadingMatchingHeadingsDTO::class,
        ReadingTrueFalseDTO::class,
        ReadingSelectiveTextCompletionDTO::class,
        ReadingFlowChartCompletionDTO::class,
        ListeningTextCompletionDTO::class,
        ListeningTableCompletionDTO::class,
        ListeningMultipleChoiceDTO::class,
        ListeningMatchingFeaturesDTO::class,
        ListeningLabellingDTO::class,
        ListeningMapCompletionDTO::class
    ]
)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "questionType"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = SpeakingQuestionDTO::class, name = "SPEAKING"),
    JsonSubTypes.Type(value = WritingQuestionDTO::class, name = "WRITING"),
    JsonSubTypes.Type(value = ReadingTextCompletionDTO::class, name = "READING_TEXT_COMPLETION"),
    JsonSubTypes.Type(value = ReadingTableCompletionDTO::class, name = "READING_TABLE_COMPLETION"),
    JsonSubTypes.Type(value = ReadingMultipleChoiceDTO::class, name = "READING_MULTIPLE_CHOICES"),
    JsonSubTypes.Type(value = ReadingMatchingFeaturesDTO::class, name = "READING_MATCHING_FEATURES"),
    JsonSubTypes.Type(value = ReadingMatchingEndingsDTO::class, name = "READING_MATCHING_ENDINGS"),
    JsonSubTypes.Type(value = ReadingMatchingHeadingsDTO::class, name = "READING_MATCHING_HEADINGS"),
    JsonSubTypes.Type(value = ReadingTrueFalseDTO::class, name = "READING_TRUE_FALSE"),
    JsonSubTypes.Type(value = ReadingSelectiveTextCompletionDTO::class, name = "READING_SELECTIVE_TEXT_COMPLETION"),
    JsonSubTypes.Type(value = ReadingFlowChartCompletionDTO::class, name = "READING_FLOWCHART_COMPLETION"),
    JsonSubTypes.Type(value = ListeningTextCompletionDTO::class, name = "LISTENING_TEXT_COMPLETION"),
    JsonSubTypes.Type(value = ListeningTableCompletionDTO::class, name = "LISTENING_TABLE_COMPLETION"),
    JsonSubTypes.Type(value = ListeningMultipleChoiceDTO::class, name = "LISTENING_MULTIPLE_CHOICES"),
    JsonSubTypes.Type(value = ListeningMatchingFeaturesDTO::class, name = "LISTENING_MATCHING_FEATURES"),
    JsonSubTypes.Type(value = ListeningLabellingDTO::class, name = "LISTENING_LABELLING"),
    JsonSubTypes.Type(value = ListeningMapCompletionDTO::class, name = "LISTENING_MAP_COMPLETION")
)
sealed class QuestionDTO(
    open val questionTypeIndex: Int? = null,
    open val header: String? = null,
    val questionType: QuestionType? = null,
    val answerType: AnswerType? = null
) {

    inline fun <reified T : Question> toQuestion(): T {
        val question = when (this) {
            is SpeakingQuestionDTO -> SpeakingQuestion(this.questionTypeIndex!!, this.header!!, this.time!!)
            is WritingQuestionDTO -> WritingQuestion(this.questionTypeIndex!!, this.header!!, this.time!!, this.content)
            is ReadingTextCompletionDTO -> ReadingTextCompletion(this.questionTypeIndex!!, this.header!!, this.text!!)
            is ReadingTableCompletionDTO -> ReadingTableCompletion(this.questionTypeIndex!!, this.header!!, this.table!!)
            is ReadingMultipleChoiceDTO -> ReadingMultipleChoice(
                this.questionTypeIndex!!,
                this.header!!,
                this.selectNum!!,
                this.issues?.map { MultipleChoiceIssue(it.index!!, it.header!!, it.description, it.options!!) }!!
            )

            is ReadingMatchingFeaturesDTO -> ReadingMatchingFeatures(
                this.questionTypeIndex!!,
                this.header!!,
                this.itemsHeader,
                this.items!!,
                this.featuresHeader,
                this.features!!
            )

            is ReadingMatchingEndingsDTO -> ReadingMatchingEndings(
                this.questionTypeIndex!!,
                this.header!!,
                this.startingPhrases!!,
                this.endingPhrases!!
            )

            is ReadingMatchingHeadingsDTO -> ReadingMatchingHeadings(this.questionTypeIndex!!, this.header!!, this.headings!!)
            is ReadingTrueFalseDTO -> ReadingTrueFalse(this.questionTypeIndex!!, this.header!!, this.issues!!)
            is ReadingSelectiveTextCompletionDTO -> ReadingSelectiveTextCompletion(
                this.questionTypeIndex!!,
                this.header!!,
                this.text!!,
                this.items!!
            )

            is ReadingFlowChartCompletionDTO -> ReadingFlowChartCompletion(this.questionTypeIndex!!, this.header!!, this.content!!, this.issues!!)

            is ListeningTextCompletionDTO -> ListeningTextCompletion(this.questionTypeIndex!!, this.header!!, this.text!!)
            is ListeningTableCompletionDTO -> ListeningTableCompletion(
                this.questionTypeIndex!!,
                this.header!!,
                this.tableHeader!!,
                this.table!!
            )

            is ListeningMultipleChoiceDTO -> ListeningMultipleChoice(
                this.questionTypeIndex!!,
                this.header!!,
                this.selectNum!!,
                this.issues!!.map { MultipleChoiceIssue(it.index!!, it.header!!, it.description, it.options!!) })

            is ListeningMatchingFeaturesDTO -> ListeningMatchingFeatures(
                this.questionTypeIndex!!,
                this.header!!,
                this.itemsHeader,
                this.items!!,
                this.featuresHeader,
                this.features!!
            )

            is ListeningLabellingDTO -> ListeningLabelling(
                this.questionTypeIndex!!,
                this.header!!,
                this.content!!,
                this.labels!!,
                this.issues!!
            )

            is ListeningMapCompletionDTO -> ListeningMapCompletion(
                this.questionTypeIndex!!,
                this.header!!,
                this.content!!,
                this.issues!!
            )
        }
        if (question !is T) throw TypeCastException("The type of question does not match the reified type.")
        return question
    }

    companion object {
        inline fun <reified T : QuestionDTO> from(question: Question): T {
            val questionDTO = when (question) {
                is SpeakingQuestion -> SpeakingQuestionDTO(question)

                is WritingQuestion -> WritingQuestionDTO(question)

                is ReadingTextCompletion -> ReadingTextCompletionDTO(question)
                is ReadingTableCompletion -> ReadingTableCompletionDTO(question)
                is ReadingMultipleChoice -> ReadingMultipleChoiceDTO(question)
                is ReadingMatchingFeatures -> ReadingMatchingFeaturesDTO(question)
                is ReadingMatchingEndings -> ReadingMatchingEndingsDTO(question)
                is ReadingMatchingHeadings -> ReadingMatchingHeadingsDTO(question)
                is ReadingTrueFalse -> ReadingTrueFalseDTO(question)
                is ReadingSelectiveTextCompletion -> ReadingSelectiveTextCompletionDTO(question)
                is ReadingFlowChartCompletion -> ReadingFlowChartCompletionDTO(question)

                is ListeningTextCompletion -> ListeningTextCompletionDTO(question)
                is ListeningTableCompletion -> ListeningTableCompletionDTO(question)
                is ListeningMultipleChoice -> ListeningMultipleChoiceDTO(question)
                is ListeningMatchingFeatures -> ListeningMatchingFeaturesDTO(question)
                is ListeningLabelling -> ListeningLabellingDTO(question)
                is ListeningMapCompletion -> ListeningMapCompletionDTO(question)
            }
            if (questionDTO !is T) throw TypeCastException("The type of question does not match the reified type.")
            return questionDTO
        }

    }
}

data class SpeakingQuestionDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val time: Long? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.SPEAKING, AnswerType.VOICE) {
    constructor(question: SpeakingQuestion) : this(
        question.index,
        question.header,
        question.time
    )
}

data class WritingQuestionDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val time: Long? = null,
    val content: String? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.WRITING, AnswerType.TEXT) {
    constructor(question: WritingQuestion) : this(
        question.index,
        question.header,
        question.time,
        question.content
    )
}

data class ReadingTextCompletionDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val text: String? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.READING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingTextCompletion) : this(
        question.index,
        question.header,
        question.text
    )
}

data class ReadingTableCompletionDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val table: List<List<String?>>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.READING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingTableCompletion) : this(
        question.index,
        question.header,
        question.table
    )
}

data class ReadingMultipleChoiceDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val selectNum: Int? = null,
    val issues: List<MultipleChoiceIssueDTO>? = null
) : QuestionDTO(
    questionTypeIndex,
    header,
    QuestionType.READING_MULTIPLE_CHOICES,
    AnswerType.TEXT_ISSUES
) {

    constructor(question: ReadingMultipleChoice) : this(
        question.index,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.index, it.header, it.description, it.options) }
    )
}

data class ReadingMatchingFeaturesDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val itemsHeader: String? = null,
    val items: List<String>? = null,
    val featuresHeader: String? = null,
    val features: List<String>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.READING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingMatchingFeatures) : this(
        question.index,
        question.header,
        question.itemsHeader,
        question.items,
        question.featuresHeader,
        question.features
    )
}

data class ReadingMatchingEndingsDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val startingPhrases: List<String>? = null,
    val endingPhrases: List<String>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.READING_MATCHING_ENDINGS, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingMatchingEndings) : this(
        question.index,
        question.header,
        question.startingPhrases,
        question.endingPhrases
    )
}

data class ReadingMatchingHeadingsDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val headings: List<String>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.READING_MATCHING_HEADINGS, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingMatchingHeadings) : this(
        question.index,
        question.header,
        question.headings
    )
}

data class ReadingTrueFalseDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val issues: List<String>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.READING_TRUE_FALSE, AnswerType.TRUE_FALSE) {
    constructor(question: ReadingTrueFalse) : this(
        question.index,
        question.header,
        question.issues
    )
}

data class ReadingSelectiveTextCompletionDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val text: String? = null,
    val items: List<String>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.READING_SELECTIVE_TEXT_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingSelectiveTextCompletion) : this(
        question.index,
        question.header,
        question.text,
        question.items
    )
}

class ReadingFlowChartCompletionDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val content: String? = null,
    val issues: List<String>? = null,
) : QuestionDTO(questionTypeIndex, header, QuestionType.READING_FLOWCHART_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingFlowChartCompletion) : this (
        question.index,
        question.header,
        question.content,
        question.issues,
    )
}

data class MultipleChoiceIssueDTO(
    val index: Int? = null,
    val header: String? = null,
    val description: String? = null,
    val options: List<String>? = null
)

data class ListeningTextCompletionDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val text: String? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.LISTENING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningTextCompletion) : this(
        question.index,
        question.header,
        question.text
    )
}

data class ListeningTableCompletionDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val tableHeader: String? = null,
    val table: List<List<String?>>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.LISTENING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningTableCompletion) : this(
        question.index,
        question.header,
        question.tableHeader,
        question.table
    )
}

data class ListeningMultipleChoiceDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val selectNum: Int? = null,
    val issues: List<MultipleChoiceIssueDTO>? = null
) : QuestionDTO(
    questionTypeIndex,
    header,
    QuestionType.LISTENING_MULTIPLE_CHOICES,
    AnswerType.TEXT
) {
    constructor(question: ListeningMultipleChoice) : this(
        question.index,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.index, it.header, it.description, it.options) }
    )
}

data class ListeningMatchingFeaturesDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val itemsHeader: String? = null,
    val items: List<String>? = null,
    val featuresHeader: String? = null,
    val features: List<String>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.LISTENING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningMatchingFeatures) : this(
        question.index,
        question.header,
        question.itemsHeader,
        question.items,
        question.featuresHeader,
        question.features
    )
}

data class ListeningLabellingDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val content: String? = null,
    val labels: List<String>? = null,
    val issues: List<String>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.LISTENING_LABELLING, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningLabelling) : this(
        question.index,
        question.header,
        question.content,
        question.labels,
        question.issues
    )
}

class ListeningMapCompletionDTO(
    override val questionTypeIndex: Int? = null,
    override val header: String? = null,
    val content: String? = null,
    val issues: List<String>? = null
) : QuestionDTO(questionTypeIndex, header, QuestionType.LISTENING_MAP_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningMapCompletion) : this(
        question.index,
        question.header,
        question.content,
        question.issues
    )
}
