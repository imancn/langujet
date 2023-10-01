package com.cn.langujet.domain.answer.model

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
    @TypeAlias("text_issues_answers")
    class TextIssues(examIssueId: String, userId: String, var text: List<String>) :
        Answer(null, examIssueId, userId, AnswerType.TEXT_ISSUES)

    @Document(collection = "answers")
    @TypeAlias("true_false_answers")
    class TrueFalse(examIssueId: String, userId: String, var isTrue: Boolean?) :
        Answer(null, examIssueId, userId, AnswerType.TRUE_FALSE)

    @Document(collection = "answers")
    @TypeAlias("voice_answers")
    class Voice(examIssueId: String, userId: String, var audioAddress: String) :
        Answer(null, examIssueId, userId, AnswerType.VOICE)
}