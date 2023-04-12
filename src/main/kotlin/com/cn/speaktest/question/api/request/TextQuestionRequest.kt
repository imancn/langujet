package com.cn.speaktest.question.api.request

import com.cn.speaktest.answer.model.AnswerType
import com.cn.speaktest.exam.model.Exam
import com.cn.speaktest.question.model.Question

class TextQuestionRequest(
    var text: String?,
    examId: String?,
    topic: String?,
    section: Int?,
    order: Int?,
    usageNumber: Int?,
    answerType: AnswerType?
) : QuestionRequest(examId, topic, section, order, usageNumber, answerType) {

    fun toQuestion(exam: Exam): Question.Text {
        return Question.Text(
            exam,
            this.topic!!,
            this.section!!,
            this.order!!,
            this.usageNumber ?: 0,
            this.answerType!!,
            this.text!!
        )
    }
}