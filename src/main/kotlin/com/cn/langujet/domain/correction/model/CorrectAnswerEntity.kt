package com.cn.langujet.domain.correction.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@Schema(
    subTypes = [
        CorrectAnswerEntity.CorrectTextAnswerEntity::class,
        CorrectAnswerEntity.CorrectTextIssuesAnswerEntity::class,
        CorrectAnswerEntity.CorrectTrueFalseAnswerEntity::class,
        CorrectAnswerEntity.CorrectMultipleChoiceAnswerEntity::class
    ]
)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = CorrectAnswerEntity.CorrectTextAnswerEntity::class, name = "TEXT"),
    JsonSubTypes.Type(value = CorrectAnswerEntity.CorrectTextIssuesAnswerEntity::class, name = "TEXT_ISSUES"),
    JsonSubTypes.Type(value = CorrectAnswerEntity.CorrectTrueFalseAnswerEntity::class, name = "TRUE_FALSE"),
    JsonSubTypes.Type(value = CorrectAnswerEntity.CorrectMultipleChoiceAnswerEntity::class, name = "MULTIPLE_CHOICE")
)
@CompoundIndexes(
    CompoundIndex(
        name = "unique_correct_answers_index",
        def = "{'examId': -1, 'sectionId': 1, 'partId': 1, 'questionId': 1}",
        unique = true
    ),
    CompoundIndex(
        name = "exam_id_section_id_index",
        def = "{'examId': -1, 'sectionId': 1}",
        unique = false
    )
)
@Document(collection = "correct_answers")
sealed class CorrectAnswerEntity(
    id: Long? = null,
    var examId: Long,
    var sectionId: Int,
    var partId: Int,
    var questionId: Int,
    var type: AnswerType,
) : HistoricalEntity(id = id) {
    @Document(collection = "correct_answers")
    @TypeAlias("correct_text_answers")
    class CorrectTextAnswerEntity(
        id: Long? = null,
        examId: Long,
        sectionId: Int,
        partId: Int,
        questionId: Int,
        var text: String
    ) : CorrectAnswerEntity(id, examId, sectionId, partId, questionId, AnswerType.TEXT)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_text_issues_answers")
    class CorrectTextIssuesAnswerEntity(
        id: Long? = null,
        examId: Long,
        sectionId: Int,
        partId: Int,
        questionId: Int,
        var issues: List<List<String>>
    ) : CorrectAnswerEntity(id, examId, sectionId, partId, questionId, AnswerType.TEXT_ISSUES)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_true_false_answers")
    class CorrectTrueFalseAnswerEntity(
        id: Long? = null,
        examId: Long,
        sectionId: Int,
        partId: Int,
        questionId: Int,
        var issues: List<TrueFalseAnswerType>
    ) : CorrectAnswerEntity(id, examId, sectionId, partId, questionId, AnswerType.TRUE_FALSE)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_multiple_choice_answers")
    class CorrectMultipleChoiceAnswerEntity(
        id: Long? = null,
        examId: Long,
        sectionId: Int,
        partId: Int,
        questionId: Int,
        var issues: List<CorrectMultipleChoiceIssueAnswer>
    ) : CorrectAnswerEntity(id, examId, sectionId, partId, questionId, AnswerType.MULTIPLE_CHOICE)

    class CorrectMultipleChoiceIssueAnswer(
        var order: Int,
        var options: List<String>
    )
}