package com.cn.langujet.domain.exam.model.question

import com.cn.langujet.domain.answer.model.AnswerType

sealed class Question(
    var index: Int,
    var header: String,
    var questionType: QuestionType,
    var answerType: AnswerType,
)

class SpeakingQuestion(
    index: Int,
    header: String,
    var time: Long,
) : Question(index, header, QuestionType.SPEAKING, AnswerType.VOICE)

class WritingQuestion(
    index: Int,
    header: String,
    var time: Long,
    var content: String?
) : Question(index, header, QuestionType.WRITING, AnswerType.TEXT)