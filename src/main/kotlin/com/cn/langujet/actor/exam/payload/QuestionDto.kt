package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import com.cn.langujet.domain.exam.model.section.part.questions.*
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
        ListeningPhotoCompletionDTO::class,
        ListeningSelectivePhotoCompletionDTO::class
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
    JsonSubTypes.Type(value = ListeningPhotoCompletionDTO::class, name = "LISTENING_PHOTO_COMPLETION"),
    JsonSubTypes.Type(value = ListeningSelectivePhotoCompletionDTO::class, name = "LISTENING_SELECTIVE_MAP_COMPLETION")
)
sealed class QuestionDTO(
    open val questionOrder: Int? = null,
    open val header: String? = null,
    val questionType: QuestionType? = null,
    val answerType: AnswerType? = null
) {
    
    inline fun <reified T : Question> toQuestion(): T {
        val question = when (this) {
            is SpeakingQuestionDTO -> SpeakingQuestion(this.questionOrder!!, this.header!!, this.audioId, this.time!!)
            is WritingQuestionDTO -> WritingQuestion(this.questionOrder!!, this.header!!, this.time!!, this.content, this.tip)
            is ReadingTextCompletionDTO -> ReadingTextCompletion(this.questionOrder!!, this.header!!, this.text!!)
            is ReadingTableCompletionDTO -> ReadingTableCompletion(this.questionOrder!!, this.header!!, this.table!!)
            is ReadingMultipleChoiceDTO -> ReadingMultipleChoice(
                this.questionOrder!!,
                this.header!!,
                this.selectNum!!,
                this.issues?.map { MultipleChoiceIssue(it.issueOrder!!, it.header!!, it.description, it.options!!) }!!
            )
            
            is ReadingMatchingFeaturesDTO -> ReadingMatchingFeatures(
                this.questionOrder!!,
                this.header!!,
                this.itemsHeader,
                this.items!!,
                this.featuresHeader,
                this.features!!
            )
            
            is ReadingMatchingEndingsDTO -> ReadingMatchingEndings(
                this.questionOrder!!,
                this.header!!,
                this.startingPhrases!!,
                this.endingPhrases!!
            )
            
            is ReadingMatchingHeadingsDTO -> ReadingMatchingHeadings(this.questionOrder!!, this.header!!, this.headings!!)
            is ReadingTrueFalseDTO -> ReadingTrueFalse(this.questionOrder!!, this.header!!, this.issues!!)
            is ReadingSelectiveTextCompletionDTO -> ReadingSelectiveTextCompletion(
                this.questionOrder!!,
                this.header!!,
                this.text!!,
                this.content,
                this.items!!
            )
            
            is ReadingFlowChartCompletionDTO -> ReadingFlowchartCompletion(this.questionOrder!!, this.header!!, this.content!!, this.issues!!)
            
            is ListeningTextCompletionDTO -> ListeningTextCompletion(this.questionOrder!!, this.header!!, this.text!!)
            is ListeningTableCompletionDTO -> ListeningTableCompletion(
                this.questionOrder!!,
                this.header!!,
                this.tableHeader!!,
                this.table!!
            )
            
            is ListeningMultipleChoiceDTO -> ListeningMultipleChoice(
                this.questionOrder!!,
                this.header!!,
                this.selectNum!!,
                this.issues!!.map { MultipleChoiceIssue(it.issueOrder!!, it.header!!, it.description, it.options!!) })
            
            is ListeningMatchingFeaturesDTO -> ListeningMatchingFeatures(
                this.questionOrder!!,
                this.header!!,
                this.itemsHeader,
                this.items!!,
                this.featuresHeader,
                this.features!!
            )
            
            is ListeningLabellingDTO -> ListeningLabelling(
                this.questionOrder!!,
                this.header!!,
                this.content!!,
                this.labels!!,
                this.issues!!
            )
            
            is ListeningPhotoCompletionDTO -> ListeningPhotoCompletion(
                this.questionOrder!!,
                this.header!!,
                this.content!!,
                this.issues!!
            )
            
            is ListeningSelectivePhotoCompletionDTO -> ListeningSelectivePhotoCompletion(
                this.questionOrder!!,
                this.header!!,
                this.content,
                this.items!!,
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
                is ReadingFlowchartCompletion -> ReadingFlowChartCompletionDTO(question)
                
                is ListeningTextCompletion -> ListeningTextCompletionDTO(question)
                is ListeningTableCompletion -> ListeningTableCompletionDTO(question)
                is ListeningMultipleChoice -> ListeningMultipleChoiceDTO(question)
                is ListeningMatchingFeatures -> ListeningMatchingFeaturesDTO(question)
                is ListeningLabelling -> ListeningLabellingDTO(question)
                is ListeningPhotoCompletion -> ListeningPhotoCompletionDTO(question)
                is ListeningSelectivePhotoCompletion -> ListeningSelectivePhotoCompletionDTO(question)
            }
            if (questionDTO !is T) throw TypeCastException("The type of question does not match the reified type.")
            return questionDTO
        }
        
    }
}

data class SpeakingQuestionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val audioId: String? = null,
    val time: Long? = null
) : QuestionDTO(questionOrder, header, QuestionType.SPEAKING, AnswerType.VOICE) {
    constructor(question: SpeakingQuestion) : this(
        question.order,
        question.header,
        question.audioId,
        question.time
    )
}

data class WritingQuestionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val time: Long? = null,
    val content: String? = null,
    val tip: String? = null
) : QuestionDTO(questionOrder, header, QuestionType.WRITING, AnswerType.TEXT) {
    constructor(question: WritingQuestion) : this(
        question.order,
        question.header,
        question.time,
        question.content,
        question.tip
    )
}

data class ReadingTextCompletionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val text: String? = null
) : QuestionDTO(questionOrder, header, QuestionType.READING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingTextCompletion) : this(
        question.order,
        question.header,
        question.text
    )
}

data class ReadingTableCompletionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val table: List<List<String?>>? = null
) : QuestionDTO(questionOrder, header, QuestionType.READING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingTableCompletion) : this(
        question.order,
        question.header,
        question.table
    )
}

data class ReadingMultipleChoiceDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val selectNum: Int? = null,
    val issues: List<MultipleChoiceIssueDTO>? = null
) : QuestionDTO(
    questionOrder,
    header,
    QuestionType.READING_MULTIPLE_CHOICES,
    AnswerType.MULTIPLE_CHOICE
) {
    
    constructor(question: ReadingMultipleChoice) : this(
        question.order,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.order, it.header, it.description, it.options) }
    )
}

data class ReadingMatchingFeaturesDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val itemsHeader: String? = null,
    val items: List<String>? = null,
    val featuresHeader: String? = null,
    val features: List<String>? = null
) : QuestionDTO(questionOrder, header, QuestionType.READING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingMatchingFeatures) : this(
        question.order,
        question.header,
        question.itemsHeader,
        question.items,
        question.featuresHeader,
        question.features
    )
}

data class ReadingMatchingEndingsDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val startingPhrases: List<String>? = null,
    val endingPhrases: List<String>? = null
) : QuestionDTO(questionOrder, header, QuestionType.READING_MATCHING_ENDINGS, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingMatchingEndings) : this(
        question.order,
        question.header,
        question.startingPhrases,
        question.endingPhrases
    )
}

data class ReadingMatchingHeadingsDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val headings: List<String>? = null
) : QuestionDTO(questionOrder, header, QuestionType.READING_MATCHING_HEADINGS, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingMatchingHeadings) : this(
        question.order,
        question.header,
        question.headings
    )
}

data class ReadingTrueFalseDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val issues: List<String>? = null
) : QuestionDTO(questionOrder, header, QuestionType.READING_TRUE_FALSE, AnswerType.TRUE_FALSE) {
    constructor(question: ReadingTrueFalse) : this(
        question.order,
        question.header,
        question.issues
    )
}

data class ReadingSelectiveTextCompletionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val text: String? = null,
    val content: String? = null,
    val items: List<String>? = null
) : QuestionDTO(questionOrder, header, QuestionType.READING_SELECTIVE_TEXT_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingSelectiveTextCompletion) : this(
        question.order,
        question.header,
        question.text,
        question.content,
        question.items
    )
}

class ReadingFlowChartCompletionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val content: String? = null,
    val issues: List<String>? = null,
) : QuestionDTO(questionOrder, header, QuestionType.READING_FLOWCHART_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ReadingFlowchartCompletion) : this(
        question.order,
        question.header,
        question.content,
        question.issues,
    )
}

data class MultipleChoiceIssueDTO(
    val issueOrder: Int? = null,
    val header: String? = null,
    val description: String? = null,
    val options: List<String>? = null
)

data class ListeningTextCompletionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val text: String? = null
) : QuestionDTO(questionOrder, header, QuestionType.LISTENING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningTextCompletion) : this(
        question.order,
        question.header,
        question.text
    )
}

data class ListeningTableCompletionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val tableHeader: String? = null,
    val table: List<List<String?>>? = null
) : QuestionDTO(questionOrder, header, QuestionType.LISTENING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningTableCompletion) : this(
        question.order,
        question.header,
        question.tableHeader,
        question.table
    )
}

data class ListeningMultipleChoiceDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val selectNum: Int? = null,
    val issues: List<MultipleChoiceIssueDTO>? = null
) : QuestionDTO(
    questionOrder,
    header,
    QuestionType.LISTENING_MULTIPLE_CHOICES,
    AnswerType.MULTIPLE_CHOICE
) {
    constructor(question: ListeningMultipleChoice) : this(
        question.order,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.order, it.header, it.description, it.options) }
    )
}

data class ListeningMatchingFeaturesDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val itemsHeader: String? = null,
    val items: List<String>? = null,
    val featuresHeader: String? = null,
    val features: List<String>? = null
) : QuestionDTO(questionOrder, header, QuestionType.LISTENING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningMatchingFeatures) : this(
        question.order,
        question.header,
        question.itemsHeader,
        question.items,
        question.featuresHeader,
        question.features
    )
}

data class ListeningLabellingDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val content: String? = null,
    val labels: List<String>? = null,
    val issues: List<String>? = null
) : QuestionDTO(questionOrder, header, QuestionType.LISTENING_LABELLING, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningLabelling) : this(
        question.order,
        question.header,
        question.content,
        question.labels,
        question.issues
    )
}

class ListeningPhotoCompletionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val content: String? = null,
    val issues: List<String>? = null
) : QuestionDTO(questionOrder, header, QuestionType.LISTENING_PHOTO_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningPhotoCompletion) : this(
        question.order,
        question.header,
        question.content,
        question.issues
    )
}

data class ListeningSelectivePhotoCompletionDTO(
    override val questionOrder: Int? = null,
    override val header: String? = null,
    val content: String? = null,
    val items: List<String>? = null,
    val issues: List<String>? = null
) : QuestionDTO(questionOrder, header, QuestionType.LISTENING_SELECTIVE_PHOTO_COMPLETION, AnswerType.TEXT_ISSUES) {
    constructor(question: ListeningSelectivePhotoCompletion) : this(
        question.order,
        question.header,
        question.content,
        question.items,
        question.issues
    )
}
