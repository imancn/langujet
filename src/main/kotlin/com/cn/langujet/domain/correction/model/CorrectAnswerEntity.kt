package com.cn.langujet.domain.correction.model

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "correct_answers")
sealed class CorrectAnswerEntity(
    @Id var id: String? = null,
    var examId: String,
    var sectionOrder: Int,
    var partOrder: Int,
    var questionOrder: Int,
    var type: AnswerType,
) {
    @Document(collection = "correct_answers")
    @TypeAlias("correct_text_answers")
    class CorrectTextAnswerEntity(
        examId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var text: String
    ) : CorrectAnswerEntity(null, examId, sectionOrder, partOrder, questionOrder, AnswerType.TEXT)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_text_issues_answers")
    class CorrectTextIssuesAnswerEntity(
        examId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var issues: List<List<String>>
    ) : CorrectAnswerEntity(null, examId, sectionOrder, partOrder, questionOrder, AnswerType.TEXT_ISSUES)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_true_false_answers")
    class CorrectTrueFalseAnswerEntity(
        examId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var issues: List<TrueFalseAnswerType>
    ) : CorrectAnswerEntity(null, examId, sectionOrder, partOrder, questionOrder, AnswerType.TRUE_FALSE)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_multiple_choice_answers")
    class CorrectMultipleChoiceAnswerEntity(
        examId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var issues: List<CorrectMultipleChoiceIssueAnswer>
    ) : CorrectAnswerEntity(null, examId, sectionOrder, partOrder, questionOrder, AnswerType.MULTIPLE_CHOICE)

    class CorrectMultipleChoiceIssueAnswer(
        var order: Int,
        var options: List<String>
    )
}