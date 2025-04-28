package com.cn.langujet.domain.exam.model.section.part.questions

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@Schema(
    subTypes = [
        SpeakingQuestionEntity::class,
        WritingQuestionEntity::class,
        ReadingTextCompletion::class,
        ReadingTableCompletion::class,
        ReadingMultipleChoice::class,
        ReadingMatchingFeatures::class,
        ReadingMatchingEndings::class,
        ReadingMatchingHeadings::class,
        ReadingTrueFalse::class,
        ReadingSelectiveTextCompletion::class,
        ReadingFlowchartCompletion::class,
        ListeningTextCompletion::class,
        ListeningTableCompletion::class,
        ListeningMultipleChoice::class,
        ListeningMatchingFeatures::class,
        ListeningLabelling::class,
        ListeningPhotoCompletion::class,
        ListeningSelectivePhotoCompletion::class,
    ]
)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "questionType"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = SpeakingQuestionEntity::class, name = "SPEAKING"),
    JsonSubTypes.Type(value = WritingQuestionEntity::class, name = "WRITING"),
    JsonSubTypes.Type(value = ReadingTextCompletion::class, name = "READING_TEXT_COMPLETION"),
    JsonSubTypes.Type(value = ReadingTableCompletion::class, name = "READING_TABLE_COMPLETION"),
    JsonSubTypes.Type(value = ReadingMultipleChoice::class, name = "READING_MULTIPLE_CHOICES"),
    JsonSubTypes.Type(value = ReadingMatchingFeatures::class, name = "READING_MATCHING_FEATURES"),
    JsonSubTypes.Type(value = ReadingMatchingEndings::class, name = "READING_MATCHING_ENDINGS"),
    JsonSubTypes.Type(value = ReadingMatchingHeadings::class, name = "READING_MATCHING_HEADINGS"),
    JsonSubTypes.Type(value = ReadingTrueFalse::class, name = "READING_TRUE_FALSE"),
    JsonSubTypes.Type(value = ReadingSelectiveTextCompletion::class, name = "READING_SELECTIVE_TEXT_COMPLETION"),
    JsonSubTypes.Type(value = ReadingFlowchartCompletion::class, name = "READING_FLOWCHART_COMPLETION"),
    JsonSubTypes.Type(value = ListeningTextCompletion::class, name = "LISTENING_TEXT_COMPLETION"),
    JsonSubTypes.Type(value = ListeningTableCompletion::class, name = "LISTENING_TABLE_COMPLETION"),
    JsonSubTypes.Type(value = ListeningMultipleChoice::class, name = "LISTENING_MULTIPLE_CHOICES"),
    JsonSubTypes.Type(value = ListeningMatchingFeatures::class, name = "LISTENING_MATCHING_FEATURES"),
    JsonSubTypes.Type(value = ListeningLabelling::class, name = "LISTENING_LABELLING"),
    JsonSubTypes.Type(value = ListeningPhotoCompletion::class, name = "LISTENING_PHOTO_COMPLETION"),
    JsonSubTypes.Type(value = ListeningSelectivePhotoCompletion::class, name = "LISTENING_SELECTIVE_MAP_COMPLETION")
)
@TypeAlias("questions")
@Document(collection = "questions")
@CompoundIndexes(
    CompoundIndex(
        name = "unique_questions_index",
        def = "{'examId': -1, 'sectionId: 1, 'partId': 1, 'order': 1}",
        unique = true
    )
)
sealed class QuestionEntity(
    id: Long?,
    var examId: Long,
    var sectionId: Long,
    var partId: Long,
    var order: Int,
    var header: String,
    var questionType: QuestionType,
    var answerType: AnswerType,
) : HistoricalEntity(id = id)

@TypeAlias("speaking_questions")
@Document(collection = "questions")
class SpeakingQuestionEntity(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var audioId: Long?,
    var time: Long,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.SPEAKING,
    answerType = AnswerType.VOICE
)

@TypeAlias("writing_questions")
@Document(collection = "questions")
class WritingQuestionEntity(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var time: Long,
    var content: String?,
    var tip: String?
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.WRITING,
    answerType = AnswerType.TEXT
)
