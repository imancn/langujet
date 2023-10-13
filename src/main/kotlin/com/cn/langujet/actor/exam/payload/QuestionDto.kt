package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.question.*
import io.swagger.v3.oas.annotations.media.Schema

@Schema(subTypes = [
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
    ListeningTextCompletionDTO::class,
    ListeningTableCompletionDTO::class,
    ListeningMultipleChoiceDTO::class,
    ListeningMatchingFeaturesDTO::class,
    ListeningLabellingDTO::class
])
sealed class QuestionDTO(
    open val index: Int,
    open val header: String,
    val questionType: QuestionType,
    val answerType: AnswerType
) {
    companion object {
        fun from(question: Question): QuestionDTO = when (question) {
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
            is ListeningTextCompletion -> ListeningTextCompletionDTO(question)
            is ListeningTableCompletion -> ListeningTableCompletionDTO(question)
            is ListeningMultipleChoice -> ListeningMultipleChoiceDTO(question)
            is ListeningMatchingFeatures -> ListeningMatchingFeaturesDTO(question)
            is ListeningLabelling -> ListeningLabellingDTO(question)
        }
    }
}
data class SpeakingQuestionDTO(
    override val index: Int,
    override val header: String,
    val time: Long
) : QuestionDTO(index, header, QuestionType.SPEAKING, AnswerType.VOICE) {
    constructor(question: SpeakingQuestion) : this(
        question.index,
        question.header,
        question.time
    )
}

data class WritingQuestionDTO(
    override val index: Int,
    override val header: String,
    val time: Long,
    val content: String?
) : QuestionDTO(index, header, QuestionType.WRITING, AnswerType.TEXT) {
    constructor(question: WritingQuestion) : this(
        question.index,
        question.header,
        question.time,
        question.content
    )
}

data class ReadingTextCompletionDTO(
    override val index: Int,
    override val header: String,
    val text: String
) : QuestionDTO(index, header, QuestionType.READING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingTextCompletion) : this(
        question.index,
        question.header,
        question.text
    )
}

data class ReadingTableCompletionDTO(
    override val index: Int,
    override val header: String,
    val table: List<List<String?>>
) : QuestionDTO(index, header, QuestionType.READING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingTableCompletion) : this(
        question.index,
        question.header,
        question.table
    )
}

data class ReadingMultipleChoiceDTO(
    override val index: Int,
    override val header: String,
    val selectNum: Int,
    val issues: List<MultipleChoiceIssueDTO>
) : QuestionDTO(index, header, QuestionType.READING_CHOICES,
    if (selectNum > 1) AnswerType.TEXT else AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingMultipleChoice) : this(
        question.index,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.header, it.description, it.options) }
    )
}

data class ReadingMatchingFeaturesDTO(
    override val index: Int,
    override val header: String,
    val itemsHeader: String?,
    val items: List<String>,
    val featuresHeader: String?,
    val features: List<String>
) : QuestionDTO(index, header, QuestionType.READING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES) {
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
    override val index: Int,
    override val header: String,
    val startingPhrases: List<String>,
    val endingPhrases: List<String>
) : QuestionDTO(index, header, QuestionType.READING_MATCHING_ENDINGS, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingMatchingEndings) : this(
        question.index,
        question.header,
        question.startingPhrases,
        question.endingPhrases
    )
}

data class ReadingMatchingHeadingsDTO(
    override val index: Int,
    override val header: String,
    val headings: List<String>
) : QuestionDTO(index, header, QuestionType.READING_MATCHING_HEADINGS, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingMatchingHeadings) : this(
        question.index,
        question.header,
        question.headings
    )
}

data class ReadingTrueFalseDTO(
    override val index: Int,
    override val header: String,
    val issues: List<String>
) : QuestionDTO(index, header, QuestionType.READING_TRUE_FALSE, AnswerType.TRUE_FALSE) {
    constructor(question: ReadingTrueFalse) : this(
        question.index,
        question.header,
        question.issues
    )
}

data class ReadingSelectiveTextCompletionDTO(
    override val index: Int,
    override val header: String,
    val text: String,
    val items: List<String>
) : QuestionDTO(index, header, QuestionType.READING_SELECTIVE_TEXT_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingSelectiveTextCompletion) : this(
        question.index,
        question.header,
        question.text,
        question.items
    )
}

data class MultipleChoiceIssueDTO(
    val header: String,
    val description: String?,
    val options: List<String>
)

data class ListeningTextCompletionDTO(
    override val index: Int,
    override val header: String,
    val text: String
) : QuestionDTO(index, header, QuestionType.LISTENING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningTextCompletion) : this(
        question.index,
        question.header,
        question.text
    )
}

data class ListeningTableCompletionDTO(
    override val index: Int,
    override val header: String,
    val tableHeader: String,
    val table: List<List<String?>>
) : QuestionDTO(index, header, QuestionType.LISTENING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningTableCompletion) : this(
        question.index,
        question.header,
        question.tableHeader,
        question.table
    )
}

data class ListeningMultipleChoiceDTO(
    override val index: Int,
    override val header: String,
    val selectNum: Int,
    val issues: List<MultipleChoiceIssueDTO>
) : QuestionDTO(index, header, QuestionType.LISTENING_MULTIPLE_CHOICE,
    if (selectNum > 1) AnswerType.TEXT else AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningMultipleChoice) : this(
        question.index,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.header, it.description, it.options) }
    )
}

data class ListeningMatchingFeaturesDTO(
    override val index: Int,
    override val header: String,
    val itemsHeader: String?,
    val items: List<String>,
    val featuresHeader: String?,
    val features: List<String>
) : QuestionDTO(index, header, QuestionType.LISTENING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES) {
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
    override val index: Int,
    override val header: String,
    val content: String,
    val labels: List<String>,
    val issues: List<String>
) : QuestionDTO(index, header, QuestionType.LISTENING_LABELLING, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningLabelling) : this(
        question.index,
        question.header,
        question.content,
        question.labels,
        question.issues
    )
}
