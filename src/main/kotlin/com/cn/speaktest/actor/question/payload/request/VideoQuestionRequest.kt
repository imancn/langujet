package com.cn.speaktest.actor.question.payload.request

import com.cn.speaktest.domain.answer.model.AnswerType
import com.cn.speaktest.domain.exam.model.ExamInfo
import com.cn.speaktest.domain.question.model.Question

class VideoQuestionRequest(
    val videoUrl: String?,
    examId: String?,
    topic: String?,
    section: Int?,
    order: Int?,
    usageNumber: Int?,
    answerType: AnswerType?
) : QuestionRequest(examId, topic, section, order, usageNumber, answerType) {

    fun toQuestion(examInfo: ExamInfo): Question.Video {
        return Question.Video(
            examInfo,
            this.topic!!,
            this.section!!,
            this.order!!,
            this.usageNumber ?: 0,
            this.answerType!!,
            this.videoUrl!!
        )
    }
}