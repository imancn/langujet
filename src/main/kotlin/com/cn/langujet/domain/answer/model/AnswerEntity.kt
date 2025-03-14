package com.cn.langujet.domain.answer.model

import com.cn.langujet.application.shared.entity.LogEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "answers")
@CompoundIndexes(
    CompoundIndex(
        name = "unique_answers_index",
        def = "{'examSessionId': -1, 'sectionOrder': 1, 'partOrder': 1, 'questionOrder': 1}",
        unique = true
    ),
    CompoundIndex(
        name = "exam_session_id_section_order_index",
        def = "{'examSessionId': -1, 'sectionOrder': 1}",
        unique = false
    )
)
sealed class AnswerEntity(
    @Id
    var id: String? = null,
    var examSessionId: String,
    var sectionOrder: Int,
    var type: AnswerType,
    var partOrder: Int,
    var questionOrder: Int,
): LogEntity() {
    @Document(collection = "answers")
    @TypeAlias("text_answers")
    class TextAnswerEntity(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var text: String
    ): AnswerEntity(null, examSessionId, sectionOrder, AnswerType.TEXT, partOrder, questionOrder)

    @Document(collection = "answers")
    @TypeAlias("text_issues_answers")
    class TextIssuesAnswerEntity(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var issues: List<String?>
    ): AnswerEntity(null, examSessionId, sectionOrder, AnswerType.TEXT_ISSUES, partOrder, questionOrder)

    @Document(collection = "answers")
    @TypeAlias("true_false_answers")
    class TrueFalseAnswerEntity(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var issues: List<TrueFalseAnswerType?>
    ): AnswerEntity(null, examSessionId, sectionOrder, AnswerType.TRUE_FALSE, partOrder, questionOrder)

    @Document(collection = "answers")
    @TypeAlias("voice_answers")
    class VoiceAnswerEntity(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var voiceFileId: String,
    ): AnswerEntity(null, examSessionId, sectionOrder, AnswerType.VOICE, partOrder, questionOrder)

    @Document(collection = "answers")
    @TypeAlias("multiple_choice_answers")
    class MultipleChoiceAnswerEntity(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var issues: List<MultipleChoiceIssueAnswer>
    ): AnswerEntity(null, examSessionId, sectionOrder, AnswerType.MULTIPLE_CHOICE, partOrder, questionOrder)
    
    @TypeAlias("multiple_choice_issue_answers")
    class MultipleChoiceIssueAnswer(
        var order: Int,
        var options: List<String?>
    )
}