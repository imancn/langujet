package com.cn.langujet.domain.answer.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "answers")
sealed class Answer(
    @Id var id: String? = null,
    var examSessionId: String,
    var sectionOrder: Int,
    var type: AnswerType,
    var partOrder: Int,
    var questionOrder: Int,
    var date: Date
) {
    @Document(collection = "answers")
    @TypeAlias("text_answers")
    class TextAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        date: Date,
        var text: String
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TEXT, partOrder, questionOrder, date)

    @Document(collection = "answers")
    @TypeAlias("text_issues_answers")
    class TextIssuesAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        date: Date,
        var issues: List<String?>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TEXT_ISSUES, partOrder, questionOrder, date)

    @Document(collection = "answers")
    @TypeAlias("true_false_answers")
    class TrueFalseAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        date: Date,
        var issues: List<TrueFalseAnswerType?>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TRUE_FALSE, partOrder, questionOrder, date)

    @Document(collection = "answers")
    @TypeAlias("voice_answers")
    class VoiceAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        date: Date,
        var voiceFileId: String,
    ): Answer(null, examSessionId, sectionOrder, AnswerType.VOICE, partOrder, questionOrder, date)

    @Document(collection = "answers")
    @TypeAlias("multiple_choice_answers")
    class MultipleChoiceAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        date: Date,
        var issues: List<MultipleChoiceIssueAnswer>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.MULTIPLE_CHOICE, partOrder, questionOrder, date)

    class MultipleChoiceIssueAnswer(
        var order: Int,
        var options: List<String?>
    )
}