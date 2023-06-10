package com.cn.speaktest.domain.question.model

import com.cn.speaktest.domain.answer.model.AnswerType
import com.cn.speaktest.domain.exam.model.Exam
import com.cn.speaktest.domain.exam.model.Section
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "questions")
sealed class Question(
    @Id var id: String?,
    @DBRef var exam: Exam,
    @DBRef var section: Section,
    var topic: String,
    var order: Int,
    var usageNumber: Int,
    var answerType: AnswerType,
    var questionType: QuestionType,
) {
    @TypeAlias("text_questions")
    @Document(collection = "questions")
    class Text(
        exam: Exam,
        section: Section,
        topic: String,
        order: Int,
        usageNumber: Int,
        answerType: AnswerType,
        var text: String
    ) : Question(null, exam, section, topic, order, usageNumber, answerType, QuestionType.TEXT)

    @TypeAlias("multiple_choice_questions")
    @Document(collection = "questions")
    class MultipleChoice(
        exam: Exam,
        section: Section,
        topic: String,
        order: Int,
        usageNumber: Int,
        answerType: AnswerType,
        var text: String,
        var choices: List<String>,
        var correctChoice: String
    ) : Question(
        null, exam, section, topic, order, usageNumber, answerType, QuestionType.CHOICE
    )

    @Document(collection = "true_false_questions")
    @TypeAlias("true_false_questions")
    class TrueFalse(
        exam: Exam,
        section: Section,
        topic: String,
        order: Int,
        usageNumber: Int,
        answerType: AnswerType,
        var text: String,
        var correctAnswer: Boolean
    ) : Question(
        null, exam, section, topic, order, usageNumber, answerType, QuestionType.TRUE_FALSE
    )

    @TypeAlias("photo_questions")
    @Document(collection = "questions")
    class Photo(
        exam: Exam,
        section: Section,
        topic: String,
        order: Int,
        usageNumber: Int,
        answerType: AnswerType,
        var photoUrl: String
    ) : Question(
        null, exam, section, topic, order, usageNumber, answerType, QuestionType.PHOTO
    )

    @TypeAlias("voice_questions")
    @Document(collection = "questions")
    class Voice(
        exam: Exam,
        section: Section,
        topic: String,
        order: Int,
        usageNumber: Int,
        answerType: AnswerType,
        var audioUrl: String,
    ) : Question(
        null, exam, section, topic, order, usageNumber, answerType, QuestionType.VOICE
    )

    @TypeAlias("video_questions")
    @Document(collection = "questions")
    class Video(
        exam: Exam,
        section: Section,
        topic: String,
        order: Int,
        usageNumber: Int,
        answerType: AnswerType,
        var videoUrl: String
    ) : Question(
        null, exam, section, topic, order, usageNumber, answerType, QuestionType.VIDEO
    )
}