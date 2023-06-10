package com.cn.speaktest.actor.question.payload.request

import com.cn.speaktest.domain.answer.model.AnswerType
import com.cn.speaktest.domain.exam.model.Exam
import com.cn.speaktest.domain.exam.model.Section
import com.cn.speaktest.domain.question.model.Question

class VideoQuestionRequest(
    val videoUrl: String?,
    examId: String?,
    sectionId: String?,
    topic: String?,
    order: Int?,
    usageNumber: Int?,
    answerType: AnswerType?
) : QuestionRequest(examId, sectionId, topic, order, usageNumber, answerType) {

    fun toQuestion(exam: Exam, section: Section): Question.Video {
        return Question.Video(
            exam,
            section,
            this.topic!!,
            this.order!!,
            this.usageNumber ?: 0,
            this.answerType!!,
            this.videoUrl!!
        )
    }
}