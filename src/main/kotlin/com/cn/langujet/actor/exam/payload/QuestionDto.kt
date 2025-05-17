package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import com.cn.langujet.domain.exam.model.section.part.questions.*
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    subTypes = [SpeakingQuestionDTO::class, WritingQuestionDTO::class, ReadingTextCompletionDTO::class, ReadingTableCompletionDTO::class, ReadingMultipleChoiceDTO::class, ReadingMatchingFeaturesDTO::class, ReadingMatchingEndingsDTO::class, ReadingMatchingHeadingsDTO::class, ReadingTrueFalseDTO::class, ReadingSelectiveTextCompletionDTO::class, ReadingFlowChartCompletionDTO::class, ListeningTextCompletionDTO::class, ListeningTableCompletionDTO::class, ListeningMultipleChoiceDTO::class, ListeningMatchingFeaturesDTO::class, ListeningLabellingDTO::class, ListeningPhotoCompletionDTO::class, ListeningSelectivePhotoCompletionDTO::class]
)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "questionType"
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
    JsonSubTypes.Type(value = ListeningSelectivePhotoCompletionDTO::class, name = "LISTENING_SELECTIVE_PHOTO_COMPLETION")
)
sealed class QuestionDTO(
    id: Long? = null,
    var questionOrder: Int,
    var header: String,
    var questionType: QuestionType,
    var answerType: AnswerType
) {
    inline fun <reified T : QuestionEntity> toQuestion(
        examId: Long, sectionId: Long, partId: Long, id: Long? = null
    ): T {
        val question = when (this) {
            is SpeakingQuestionDTO -> SpeakingQuestionEntity(
                id, examId, sectionId, partId, questionOrder, header, audioId, time
            )
            
            is WritingQuestionDTO -> WritingQuestionEntity(
                id, examId, sectionId, partId, questionOrder, header, time, content, tip
            )
            
            is ReadingTextCompletionDTO -> ReadingTextCompletion(
                id, examId, sectionId, partId, questionOrder, header, text
            )
            
            is ReadingTableCompletionDTO -> ReadingTableCompletion(
                id, examId, sectionId, partId, questionOrder, header, table
            )
            
            is ReadingMultipleChoiceDTO -> ReadingMultipleChoice(
                id,
                examId,
                sectionId,
                partId,
                questionOrder,
                header,
                selectNum,
                issues.map { MultipleChoiceIssue(it.issueOrder, it.header, it.description, it.options) })
            
            is ReadingMatchingFeaturesDTO -> ReadingMatchingFeatures(
                id, examId, sectionId, partId, questionOrder, header, itemsHeader, items, featuresHeader, features
            )
            
            is ReadingMatchingEndingsDTO -> ReadingMatchingEndings(
                id, examId, sectionId, partId, questionOrder, header, startingPhrases, endingPhrases
            )
            
            is ReadingMatchingHeadingsDTO -> ReadingMatchingHeadings(
                id, examId, sectionId, partId, questionOrder, header, headings
            )
            
            is ReadingTrueFalseDTO -> ReadingTrueFalse(
                id, examId, sectionId, partId, questionOrder, header, issues
            )
            
            is ReadingSelectiveTextCompletionDTO -> ReadingSelectiveTextCompletion(
                id, examId, sectionId, partId, questionOrder, header, text, content, items
            )
            
            is ReadingFlowChartCompletionDTO -> ReadingFlowchartCompletion(
                id, examId, sectionId, partId, questionOrder, header, content, issues
            )
            
            is ListeningTextCompletionDTO -> ListeningTextCompletion(
                id, examId, sectionId, partId, questionOrder, header, text
            )
            
            is ListeningTableCompletionDTO -> ListeningTableCompletion(
                id, examId, sectionId, partId, questionOrder, header, tableHeader, table
            )
            
            is ListeningMultipleChoiceDTO -> ListeningMultipleChoice(
                id,
                examId,
                sectionId,
                partId,
                questionOrder,
                header,
                selectNum,
                issues.map { MultipleChoiceIssue(it.issueOrder, it.header, it.description, it.options) })
            
            is ListeningMatchingFeaturesDTO -> ListeningMatchingFeatures(
                id, examId, sectionId, partId, questionOrder, header, itemsHeader, items, featuresHeader, features
            )
            
            is ListeningLabellingDTO -> ListeningLabelling(
                id, examId, sectionId, partId, questionOrder, header, content, labels, issues
            )
            
            is ListeningPhotoCompletionDTO -> ListeningPhotoCompletion(
                id, examId, sectionId, partId, questionOrder, header, content, issues
            )
            
            is ListeningSelectivePhotoCompletionDTO -> ListeningSelectivePhotoCompletion(
                id, examId, sectionId, partId, questionOrder, header, content, items, issues
            )
        }
        if (question !is T) throw TypeCastException("The type of question does not match the reified type.")
        return question
    }
    
    companion object {
        inline fun <reified T : QuestionDTO> from(question: QuestionEntity): T {
            val questionDTO = when (question) {
                is SpeakingQuestionEntity -> SpeakingQuestionDTO(question)
                is WritingQuestionEntity -> WritingQuestionDTO(question)
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

class SpeakingQuestionDTO(
    questionOrder: Int, header: String, var audioId: Long?, var time: Long
) : QuestionDTO(
    questionOrder = questionOrder, header = header, questionType = QuestionType.SPEAKING, answerType = AnswerType.VOICE
) {
    constructor(question: SpeakingQuestionEntity) : this(
        questionOrder = question.order, header = question.header, audioId = question.audioId, time = question.time
    )
    
    constructor(question: SpeakingQuestionEntity, id: Long) : this(
        questionOrder = question.order, header = question.header, audioId = question.audioId, time = question.time
    )
}

class WritingQuestionDTO(
    id: Long? = null, questionOrder: Int, header: String, var time: Long, var content: String?, var tip: String?
) : QuestionDTO(id, questionOrder, header, QuestionType.WRITING, AnswerType.TEXT) {
    constructor(question: WritingQuestionEntity) : this(
        question.id, question.order, question.header, question.time, question.content, question.tip
    )
    
    constructor(question: WritingQuestionEntity, id: Long) : this(
        question.id, question.order, question.header, question.time, question.content, question.tip
    )
}

class ReadingTextCompletionDTO(
    id: Long? = null, questionOrder: Int, header: String, var text: String
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.READING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES
) {
    constructor(question: ReadingTextCompletion) : this(
        question.id, question.order, question.header, question.text
    )
    
    constructor(question: ReadingTextCompletion, id: Long) : this(
        question.id, question.order, question.header, question.text
    )
}

class ReadingTableCompletionDTO(
    id: Long? = null, questionOrder: Int, header: String, var table: List<List<String?>>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.READING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES
) {
    constructor(question: ReadingTableCompletion) : this(
        question.id, question.order, question.header, question.table
    )
    
    constructor(question: ReadingTableCompletion, id: Long) : this(
        question.id, question.order, question.header, question.table
    )
}

class ReadingMultipleChoiceDTO(
    id: Long? = null, questionOrder: Int, header: String, var selectNum: Int, var issues: List<MultipleChoiceIssueDTO>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.READING_MULTIPLE_CHOICES, AnswerType.MULTIPLE_CHOICE
) {
    constructor(question: ReadingMultipleChoice) : this(
        question.id,
        question.order,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.order, it.header, it.description, it.options) })
    
    constructor(question: ReadingMultipleChoice, id: Long) : this(
        question.id,
        question.order,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.order, it.header, it.description, it.options) })
}

class ReadingMatchingFeaturesDTO(
    id: Long? = null,
    questionOrder: Int,
    header: String,
    var itemsHeader: String?,
    var items: List<String>,
    var featuresHeader: String?,
    var features: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.READING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES
) {
    constructor(question: ReadingMatchingFeatures) : this(
        question.id,
        question.order,
        question.header,
        question.itemsHeader,
        question.items,
        question.featuresHeader,
        question.features
    )
    
    constructor(question: ReadingMatchingFeatures, id: Long) : this(
        question.id,
        question.order,
        question.header,
        question.itemsHeader,
        question.items,
        question.featuresHeader,
        question.features
    )
}

class ReadingMatchingEndingsDTO(
    id: Long? = null,
    questionOrder: Int,
    header: String,
    var startingPhrases: List<String>,
    var endingPhrases: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.READING_MATCHING_ENDINGS, AnswerType.TEXT_ISSUES
) {
    constructor(question: ReadingMatchingEndings) : this(
        question.id, question.order, question.header, question.startingPhrases, question.endingPhrases
    )
    
    constructor(question: ReadingMatchingEndings, id: Long) : this(
        question.id, question.order, question.header, question.startingPhrases, question.endingPhrases
    )
}

class ReadingMatchingHeadingsDTO(
    id: Long? = null, questionOrder: Int, header: String, var headings: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.READING_MATCHING_HEADINGS, AnswerType.TEXT_ISSUES
) {
    constructor(question: ReadingMatchingHeadings) : this(
        question.id, question.order, question.header, question.headings
    )
    
    constructor(question: ReadingMatchingHeadings, id: Long) : this(
        question.id, question.order, question.header, question.headings
    )
}

class ReadingTrueFalseDTO(
    id: Long? = null, questionOrder: Int, header: String, var issues: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.READING_TRUE_FALSE, AnswerType.TRUE_FALSE
) {
    constructor(question: ReadingTrueFalse) : this(
        question.id, question.order, question.header, question.issues
    )
    
    constructor(question: ReadingTrueFalse, id: Long) : this(
        question.id, question.order, question.header, question.issues
    )
}

class ReadingSelectiveTextCompletionDTO(
    id: Long? = null,
    questionOrder: Int,
    header: String,
    var text: String,
    var content: String?,
    var items: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.READING_SELECTIVE_TEXT_COMPLETION, AnswerType.TEXT_ISSUES
) {
    constructor(question: ReadingSelectiveTextCompletion) : this(
        question.id, question.order, question.header, question.text, question.content, question.items
    )
    
    constructor(question: ReadingSelectiveTextCompletion, id: Long) : this(
        question.id, question.order, question.header, question.text, question.content, question.items
    )
}

class ReadingFlowChartCompletionDTO(
    id: Long? = null, questionOrder: Int, header: String, var content: String, var issues: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.READING_FLOWCHART_COMPLETION, AnswerType.TEXT_ISSUES
) {
    constructor(question: ReadingFlowchartCompletion) : this(
        question.id, question.order, question.header, question.content, question.issues
    )
    
    constructor(question: ReadingFlowchartCompletion, id: Long) : this(
        question.id, question.order, question.header, question.content, question.issues
    )
}

class ListeningTextCompletionDTO(
    id: Long? = null, questionOrder: Int, header: String, var text: String
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.LISTENING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES
) {
    constructor(question: ListeningTextCompletion) : this(
        question.id, question.order, question.header, question.text
    )
    
    constructor(question: ListeningTextCompletion, id: Long) : this(
        question.id, question.order, question.header, question.text
    )
}

class ListeningTableCompletionDTO(
    id: Long? = null, questionOrder: Int, header: String, var tableHeader: String, var table: List<List<String?>>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.LISTENING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES
) {
    constructor(question: ListeningTableCompletion) : this(
        question.id, question.order, question.header, question.tableHeader, question.table
    )
    
    constructor(question: ListeningTableCompletion, id: Long) : this(
        question.id, question.order, question.header, question.tableHeader, question.table
    )
}

class ListeningMultipleChoiceDTO(
    id: Long? = null, questionOrder: Int, header: String, var selectNum: Int, var issues: List<MultipleChoiceIssueDTO>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.LISTENING_MULTIPLE_CHOICES, AnswerType.MULTIPLE_CHOICE
) {
    constructor(question: ListeningMultipleChoice) : this(
        question.id,
        question.order,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.order, it.header, it.description, it.options) })
    
    constructor(question: ListeningMultipleChoice, id: Long) : this(
        question.id,
        question.order,
        question.header,
        question.selectNum,
        question.issues.map { MultipleChoiceIssueDTO(it.order, it.header, it.description, it.options) })
}

class ListeningMatchingFeaturesDTO(
    id: Long? = null,
    questionOrder: Int,
    header: String,
    var itemsHeader: String?,
    var items: List<String>,
    var featuresHeader: String?,
    var features: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.LISTENING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES
) {
    constructor(question: ListeningMatchingFeatures) : this(
        question.id,
        question.order,
        question.header,
        question.itemsHeader,
        question.items,
        question.featuresHeader,
        question.features
    )
    
    constructor(question: ListeningMatchingFeatures, id: Long) : this(
        question.id,
        question.order,
        question.header,
        question.itemsHeader,
        question.items,
        question.featuresHeader,
        question.features
    )
}

class ListeningLabellingDTO(
    id: Long? = null,
    questionOrder: Int,
    header: String,
    var content: String,
    var labels: List<String>,
    var issues: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.LISTENING_LABELLING, AnswerType.TEXT_ISSUES
) {
    constructor(question: ListeningLabelling) : this(
        question.id, question.order, question.header, question.content, question.labels, question.issues
    )
    
    constructor(question: ListeningLabelling, id: Long) : this(
        question.id, question.order, question.header, question.content, question.labels, question.issues
    )
}

class ListeningPhotoCompletionDTO(
    id: Long? = null, questionOrder: Int, header: String, var content: String, var issues: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.LISTENING_PHOTO_COMPLETION, AnswerType.TEXT_ISSUES
) {
    constructor(question: ListeningPhotoCompletion) : this(
        question.id, question.order, question.header, question.content, question.issues
    )
    
    constructor(question: ListeningPhotoCompletion, id: Long) : this(
        question.id, question.order, question.header, question.content, question.issues
    )
}

class ListeningSelectivePhotoCompletionDTO(
    id: Long? = null,
    questionOrder: Int,
    header: String,
    var content: String?,
    var items: List<String>,
    var issues: List<String>
) : QuestionDTO(
    id = id, questionOrder, header, QuestionType.LISTENING_SELECTIVE_PHOTO_COMPLETION, AnswerType.TEXT_ISSUES
) {
    constructor(question: ListeningSelectivePhotoCompletion) : this(
        question.id, question.order, question.header, question.content, question.items, question.issues
    )
    
    constructor(question: ListeningSelectivePhotoCompletion, id: Long) : this(
        question.id, question.order, question.header, question.content, question.items, question.issues
    )
}

data class MultipleChoiceIssueDTO(
    val issueOrder: Int, val header: String, val description: String?, val options: List<String>
)
