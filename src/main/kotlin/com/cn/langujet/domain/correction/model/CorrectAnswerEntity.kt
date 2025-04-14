package com.cn.langujet.domain.correction.model

import com.cn.langujet.application.arch.models.Historical
import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@CompoundIndexes(
    CompoundIndex(
        name = "unique_correct_answers_index",
        def = "{'examId': -1, 'sectionOrder': 1, 'partOrder': 1, 'questionOrder': 1}",
        unique = true
    ),
    CompoundIndex(
        name = "exam_id_section_order_index",
        def = "{'examId': -1, 'sectionOrder': 1}",
        unique = false
    )
)
@Document(collection = "correct_answers")
sealed class CorrectAnswerEntity(
    @Id var id: String? = null,
    var examId: String,
    var sectionOrder: Int,
    var partOrder: Int,
    var questionOrder: Int,
    var type: AnswerType,
) : Historical() {
    @Document(collection = "correct_answers")
    @TypeAlias("correct_text_answers")
    class CorrectTextAnswerEntity(
        id: String? = null,
        examId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var text: String
    ) : CorrectAnswerEntity(id, examId, sectionOrder, partOrder, questionOrder, AnswerType.TEXT)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_text_issues_answers")
    class CorrectTextIssuesAnswerEntity(
        id: String? = null,
        examId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var issues: List<List<String>>
    ) : CorrectAnswerEntity(id, examId, sectionOrder, partOrder, questionOrder, AnswerType.TEXT_ISSUES)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_true_false_answers")
    class CorrectTrueFalseAnswerEntity(
        id: String? = null,
        examId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var issues: List<TrueFalseAnswerType>
    ) : CorrectAnswerEntity(id, examId, sectionOrder, partOrder, questionOrder, AnswerType.TRUE_FALSE)

    @Document(collection = "correct_answers")
    @TypeAlias("correct_multiple_choice_answers")
    class CorrectMultipleChoiceAnswerEntity(
        id: String? = null,
        examId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        var issues: List<CorrectMultipleChoiceIssueAnswer>
    ) : CorrectAnswerEntity(id, examId, sectionOrder, partOrder, questionOrder, AnswerType.MULTIPLE_CHOICE)

    class CorrectMultipleChoiceIssueAnswer(
        var order: Int,
        var options: List<String>
    )
}