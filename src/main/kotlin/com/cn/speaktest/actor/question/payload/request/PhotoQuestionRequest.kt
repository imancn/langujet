package com.cn.speaktest.actor.question.payload.request

import com.cn.speaktest.domain.answer.model.AnswerType
import com.cn.speaktest.domain.exam.model.ExamMeta
import com.cn.speaktest.domain.question.model.Question

class PhotoQuestionRequest(
    var photoUrl: String?,
    examId: String?,
    topic: String?,
    section: Int?,
    order: Int?,
    usageNumber: Int?,
    answerType: AnswerType?
) : QuestionRequest(examId, topic, section, order, usageNumber, answerType) {

    fun toQuestion(examMeta: ExamMeta): Question.Photo {
        return Question.Photo(
            examMeta,
            this.topic!!,
            this.section!!,
            this.order!!,
            this.usageNumber ?: 0,
            this.answerType!!,
            this.photoUrl!!
        )
    }
}