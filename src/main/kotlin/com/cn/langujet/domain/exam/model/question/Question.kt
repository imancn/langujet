package com.cn.langujet.domain.exam.model.question

import com.cn.langujet.domain.answer.model.AnswerType

sealed class Question(
    var order: Int,
    var header: String,
    var questionType: QuestionType,
    var answerType: AnswerType,
)

class SpeakingQuestion(
    order: Int,
    header: String,
    var audioId: String?,
    var time: Long,
) : Question(order, header, QuestionType.SPEAKING, AnswerType.VOICE)

class WritingQuestion(
    order: Int,
    header: String,
    var time: Long,
    var content: String?,
    var tip: String?
) : Question(order, header, QuestionType.WRITING, AnswerType.TEXT)