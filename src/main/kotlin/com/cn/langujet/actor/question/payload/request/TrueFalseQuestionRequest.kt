package com.cn.langujet.actor.question.payload.request

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.question.model.Question

class TrueFalseQuestionRequest(
    var text: String?,
    var correctAnswer: Boolean?,
    examId: String?,
    sectionId: String?,
    topic: String?,
    order: Int?,
    usageNumber: Int?,
    answerType: AnswerType?
) : QuestionRequest(examId, sectionId, topic, order, usageNumber, answerType) {

    fun toQuestion(exam: Exam, section: Section): Question.TrueFalse {
        return Question.TrueFalse(
            exam,
            section,
            this.topic!!,
            this.order!!,
            this.usageNumber!!,
            this.answerType!!,
            this.text!!,
            this.correctAnswer!!
        )
    }
}