package com.cn.langujet.domain.exam.model.section.part.questions

import com.cn.langujet.application.arch.mongo.models.SequentialEntity
import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@TypeAlias("questions")
@Document(collection = "questions")
sealed class QuestionEntity(
    id: Long?,
    var examId: String,
    var sectionId: String,
    var partId: Long,
    var order: Int,
    var header: String,
    var questionType: QuestionType,
    var answerType: AnswerType,
) : SequentialEntity(id = id)

@TypeAlias("speaking_questions")
@Document(collection = "questions")
class SpeakingQuestionEntity(
    id: Long?,
    examId: String,
    sectionId: String,
    partId: Long,
    order: Int,
    header: String,
    var audioId: String?,
    var time: Long,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.SPEAKING,
    answerType = AnswerType.VOICE
)

@TypeAlias("writing_questions")
@Document(collection = "questions")
class WritingQuestionEntity(
    id: Long?,
    examId: String,
    sectionId: String,
    partId: Long,
    order: Int,
    header: String,
    var time: Long,
    var content: String?,
    var tip: String?
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.WRITING,
    answerType = AnswerType.TEXT
)
