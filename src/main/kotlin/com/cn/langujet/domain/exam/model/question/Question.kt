package com.cn.langujet.domain.exam.model.question

import com.cn.langujet.domain.answer.model.AnswerType

sealed class Question(
    var id: Int,
    var header: String,
    var questionType: QuestionType,
    var answerType: AnswerType,
)

class SpeakingQuestion(
    id: Int,
    header: String,
    var time: Long,
) : Question(id, header, QuestionType.SPEAKING, AnswerType.VOICE)

class WritingQuestion(
    id: Int,
    header: String,
    var time: Long,
    var content: String?
) : Question(id, header, QuestionType.WRITING, AnswerType.TEXT)