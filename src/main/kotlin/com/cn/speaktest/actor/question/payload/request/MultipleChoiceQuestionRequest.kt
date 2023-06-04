package com.cn.speaktest.actor.question.payload.request

import com.cn.speaktest.domain.answer.model.AnswerType
import com.cn.speaktest.domain.exam.model.ExamInfo
import com.cn.speaktest.domain.question.model.Question

class MultipleChoiceQuestionRequest(
    var text: String?,
    var choices: List<String>?,
    var correctChoice: String?,
    examId: String?,
    topic: String?,
    section: Int?,
    order: Int?,
    usageNumber: Int?,
    answerType: AnswerType?
) : QuestionRequest(examId, topic, section, order, usageNumber, answerType) {

    fun toQuestion(examInfo: ExamInfo): Question.MultipleChoice {
        return Question.MultipleChoice(
            examInfo,
            this.topic!!,
            this.section!!,
            this.order!!,
            this.usageNumber ?: 0,
            this.answerType!!,
            this.text!!,
            this.choices!!,
            this.correctChoice!!
        )
    }
}