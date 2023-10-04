package com.cn.langujet.domain.exam.model.question

import com.cn.langujet.domain.answer.model.AnswerType

sealed class Question(
    var index: Int,
    var time: Long, // seconds
    var header: String,
    var questionType: QuestionType,
    var answerType: AnswerType,
)

class SpeakingQuestion(
    index: Int,
    time: Long,
    header: String,
) : Question(index, time, header, QuestionType.SPEAKING, AnswerType.VOICE)

class WritingQuestion(
    index: Int,
    time: Long,
    header: String,
    var content: String?
) : Question(index, time, header, QuestionType.WRITING, AnswerType.TEXT)