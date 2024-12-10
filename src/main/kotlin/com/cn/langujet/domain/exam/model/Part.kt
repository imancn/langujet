package com.cn.langujet.domain.exam.model

import com.cn.langujet.domain.exam.model.question.Question
import com.cn.langujet.domain.exam.model.question.SpeakingQuestion
import com.cn.langujet.domain.exam.model.question.WritingQuestion

sealed class Part(
    var order: Int,
    var type: SectionType,
) {
    abstract fun getQuestions(): List<Question>
}

class ReadingPart(
    order: Int,
    var passageHeader: String?,
    var passage: List<Passage>,
    var questionList: List<Question>
) : Part(order, SectionType.READING) {
    override fun getQuestions(): List<Question> {
        return questionList
    }
}

class Passage(
    var indicator: String?,
    var paragraph: String
)

class ListeningPart(
    order: Int,
    var audioId: String,
    var questionList: List<Question>,
    var time: Long
) : Part(order, SectionType.LISTENING) {
    override fun getQuestions(): List<Question> {
        return questionList
    }
}

class WritingPart(
    order: Int,
    var question: WritingQuestion
) : Part(order, SectionType.WRITING) {
    override fun getQuestions(): List<Question> {
        return listOf(question)
    }
}

class SpeakingPart(
    order: Int,
    var questionList: List<SpeakingQuestion>,
    var focus: String?
) : Part(order, SectionType.SPEAKING) {
    override fun getQuestions(): List<Question> {
        return questionList
    }
}
