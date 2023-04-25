package com.cn.speaktest.domain.answer.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "answers")
sealed class Answer(
    @Id var id: String? = null,
    var examIssueId: String,
    var userId: String,
    var type: AnswerType,
) {
    @Document(collection = "answers")
    @TypeAlias("text_answers")
    class Text(examIssueId: String, userId: String, var text: String) :
        Answer(null, examIssueId, userId, AnswerType.TEXT)

    @Document(collection = "answers")
    @TypeAlias("choice_answers")
    class Choice(examIssueId: String, userId: String, var choice: String) :
        Answer(null, examIssueId, userId, AnswerType.CHOICE)

    @Document(collection = "answers")
    @TypeAlias("true_false_answers")
    class TrueFalse(examIssueId: String, userId: String, var isTrue: Boolean) :
        Answer(null, examIssueId, userId, AnswerType.TRUE_FALSE)

    @Document(collection = "answers")
    @TypeAlias("voice_answers")
    class Voice(examIssueId: String, userId: String, var audioUrl: String) :
        Answer(null, examIssueId, userId, AnswerType.VOICE)
}