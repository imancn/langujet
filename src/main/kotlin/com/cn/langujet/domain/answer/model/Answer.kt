package com.cn.langujet.domain.answer.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "answers")
sealed class Answer(
    @Id var id: String? = null,
    var examSessionId: String,
    var sectionId: String,
    var type: AnswerType,
    var partIndex: Int,
    var questionIndex: Int,
) {
    @Document(collection = "answers")
    @TypeAlias("text_answers")
    class Text(
        examSessionId: String,
        sectionId: String,
        partIndex: Int,
        questionIndex: Int,
        var text: String
    ): Answer(null, examSessionId, sectionId, AnswerType.TEXT, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("text_parts_answers")
    class TextIssues(
        examSessionId: String,
        sectionId: String,
        partIndex: Int,
        questionIndex: Int,
        var textList: List<String?>
    ): Answer(null, examSessionId, sectionId, AnswerType.TEXT_ISSUES, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("true_false_answers")
    class TrueFalse(
        examSessionId: String,
        sectionId: String,
        partIndex: Int,
        questionIndex: Int,
        var booleanList: List<Boolean?>
    ): Answer(null, examSessionId, sectionId, AnswerType.TRUE_FALSE, partIndex, questionIndex)

    @Document(collection = "answers")
    @TypeAlias("voice_answers")
    class Voice(
        examSessionId: String,
        sectionId: String,
        partIndex: Int,
        questionIndex: Int,
        var audioAddress: String
    ): Answer(null, examSessionId, sectionId, AnswerType.VOICE, partIndex, questionIndex)
}