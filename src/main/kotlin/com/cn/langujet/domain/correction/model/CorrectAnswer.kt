package com.cn.langujet.domain.correction.model

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "correct_answers")
sealed class CorrectAnswer(
    @Id var id: String? = null,
    var examId: String,
    var sectionOrder: Int,
    var partId: Int,
    var questionId: Int,
    var type: AnswerType,
) {
    @Document(collection = "correct_answers")
    @TypeAlias("correct_text_answers")
    class CorrectTextAnswer(
        examId: String,
        sectionOrder: Int,
        partId: Int,
        questionId: Int,
        var text: String
    ) : CorrectAnswer(null, examId, sectionOrder, partId, questionId, AnswerType.TEXT)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_text_issues_answers")
    class CorrectTextIssuesAnswer(
        examId: String,
        sectionOrder: Int,
        partId: Int,
        questionId: Int,
        var textList: List<String>
    ) : CorrectAnswer(null, examId, sectionOrder, partId, questionId, AnswerType.TEXT_ISSUES)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_true_false_answers")
    class CorrectTrueFalseAnswer(
        examId: String,
        sectionOrder: Int,
        partId: Int,
        questionId: Int,
        var answers: List<TrueFalseAnswerType>
    ) : CorrectAnswer(null, examId, sectionOrder, partId, questionId, AnswerType.TRUE_FALSE)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_multiple_choice_answers")
    class CorrectMultipleChoiceAnswer(
        examId: String,
        sectionOrder: Int,
        partId: Int,
        questionId: Int,
        var issues: List<CorrectMultipleChoiceIssueAnswer>
    ) : CorrectAnswer(null, examId, sectionOrder, partId, questionId, AnswerType.MULTIPLE_CHOICE)

    class CorrectMultipleChoiceIssueAnswer(
        var id: Int,
        var options: List<String>
    )
}