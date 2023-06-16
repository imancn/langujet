package com.cn.langujet.actor.question.payload.request

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.question.model.Question

class MultipleChoiceQuestionRequest(
    var text: String?,
    var choices: List<String>?,
    var correctChoice: String?,
    examId: String?,
    sectionId: String?,
    topic: String?,
    order: Int?,
    usageNumber: Int?,
    answerType: AnswerType?
) : QuestionRequest(examId, sectionId, topic, order, usageNumber, answerType) {

    fun toQuestion(exam: Exam, section: Section): Question.MultipleChoice {
        return Question.MultipleChoice(
            exam,
            section,
            this.topic!!,
            this.order!!,
            this.usageNumber ?: 0,
            this.answerType!!,
            this.text!!,
            this.choices!!,
            this.correctChoice!!
        )
    }
}