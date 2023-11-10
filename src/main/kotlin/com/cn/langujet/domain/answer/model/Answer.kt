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
    class Text(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        var text: String
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TEXT, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("text_parts_answers")
    class TextIssues(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        var textList: List<String?>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TEXT_ISSUES, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("true_false_answers")
    class TrueFalse(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        var booleanList: List<Boolean?>
    ): Answer(null, examSessionId, sectionOrder, AnswerType.TRUE_FALSE, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("voice_answers")
    class Voice(
        examSessionId: String,
        sectionOrder: Int,
        partIndex: Int,
        questionIndex: Int,
        var audioId: String
    ): Answer(null, examSessionId, sectionOrder, AnswerType.VOICE, partIndex, questionIndex)
}