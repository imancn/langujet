package com.cn.langujet.actor.question.payload.request

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.question.model.Question

class PhotoQuestionRequest(
    var photoUrl: String?,
    examId: String?,
    topic: String?,
    sectionId: String?,
    order: Int?,
    usageNumber: Int?,
    answerType: AnswerType?
) : QuestionRequest(examId, sectionId, topic, order, usageNumber, answerType) {

    fun toQuestion(exam: Exam, section: Section): Question.Photo {
        return Question.Photo(
            exam,
            section,
            this.topic!!,
            this.order!!,
            this.usageNumber ?: 0,
            this.answerType!!,
            this.photoUrl!!
        )
    }
}