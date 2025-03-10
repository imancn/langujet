package com.cn.langujet.domain.exam.model.section.part.questions

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import org.springframework.data.annotation.TypeAlias

@TypeAlias("questions")
sealed class Question(
    var order: Int,
    var header: String,
    var questionType: QuestionType,
    var answerType: AnswerType,
)

@TypeAlias("speaking_questions")
class SpeakingQuestion(
    order: Int,
    header: String,
    var audioId: String?,
    var time: Long,
) : Question(order, header, QuestionType.SPEAKING, AnswerType.VOICE)

@TypeAlias("writing_questions")
class WritingQuestion(
    order: Int,
    header: String,
    var time: Long,
    var content: String?,
    var tip: String?
) : Question(order, header, QuestionType.WRITING, AnswerType.TEXT)