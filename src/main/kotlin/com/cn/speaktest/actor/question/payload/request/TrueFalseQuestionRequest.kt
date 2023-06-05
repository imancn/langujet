package com.cn.speaktest.actor.question.payload.request

import com.cn.speaktest.domain.answer.model.AnswerType
import com.cn.speaktest.domain.exam.model.ExamMeta
import com.cn.speaktest.domain.question.model.Question

class TrueFalseQuestionRequest(
    var text: String?,
    var correctAnswer: Boolean?,
    examId: String?,
    topic: String?,
    section: Int?,
    order: Int?,
    usageNumber: Int?,
    answerType: AnswerType?
) : QuestionRequest(examId, topic, section, order, usageNumber, answerType) {

    fun toQuestion(examMeta: ExamMeta): Question.TrueFalse {
        return Question.TrueFalse(
            examMeta,
            this.topic!!,
            this.section!!,
            this.order!!,
            this.usageNumber!!,
            this.answerType!!,
            this.text!!,
            this.correctAnswer!!
        )
    }
}