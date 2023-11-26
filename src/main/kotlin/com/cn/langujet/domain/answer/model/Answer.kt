package com.cn.langujet.domain.answer.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "answers")
sealed class Answer(
    @Id var id: String? = null,
    var examSessionId: String,
    var sectionOrder: Int,
    var type: AnswerType,
    var partIndex: Int,
    var questionIndex: Int,
) {
    @Document(collection = "answers")
    @TypeAlias("text_answers")
    class TextAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        var text: String
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TEXT, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("text_issues_answers")
    class TextIssuesAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        var textList: List<String?>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TEXT_ISSUES, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("true_false_answers")
    class TrueFalseAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        var answers: List<TrueFalseAnswerType?>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TRUE_FALSE, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("voice_answers")
    class VoiceAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        var audioId: String
    ): Answer(null, examSessionId, sectionOrder, AnswerType.VOICE, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("multiple_choice_answers")
    class MultipleChoiceAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        var issues: List<MultipleChoiceIssueAnswer>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.MULTIPLE_CHOICE, partIndex, questionIndex)

    class MultipleChoiceIssueAnswer(
        var index: Int,
        var options: List<String?>
    )
}