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
    var partIndex: Int,
    var questionIndex: Int,
    var date: Date
) {
    @Document(collection = "answers")
    @TypeAlias("text_answers")
    class TextAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        date: Date,
        var text: String
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TEXT, partIndex, questionIndex, date)

    @Document(collection = "answers")
    @TypeAlias("text_issues_answers")
    class TextIssuesAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        date: Date,
        var textList: List<String?>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TEXT_ISSUES, partIndex, questionIndex, date)

    @Document(collection = "answers")
    @TypeAlias("true_false_answers")
    class TrueFalseAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        date: Date,
        var answers: List<TrueFalseAnswerType?>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TRUE_FALSE, partIndex, questionIndex, date)

    @Document(collection = "answers")
    @TypeAlias("voice_answers")
    class VoiceAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        date: Date,
        var audioId: String
    ): Answer(null, examSessionId, sectionOrder, AnswerType.VOICE, partIndex, questionIndex, date)

    @Document(collection = "answers")
    @TypeAlias("multiple_choice_answers")
    class MultipleChoiceAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        date: Date,
        var issues: List<MultipleChoiceIssueAnswer>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.MULTIPLE_CHOICE, partIndex, questionIndex, date)

    class MultipleChoiceIssueAnswer(
        var index: Int,
        var options: List<String?>
    )
}