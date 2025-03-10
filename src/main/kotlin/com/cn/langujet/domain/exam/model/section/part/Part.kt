package com.cn.langujet.domain.exam.model.section.part

import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.model.section.part.questions.Question
import com.cn.langujet.domain.exam.model.section.part.questions.SpeakingQuestion
import com.cn.langujet.domain.exam.model.section.part.questions.WritingQuestion
import org.springframework.data.annotation.TypeAlias

@TypeAlias("part")
sealed class Part(
    var order: Int = 1,
    var type: SectionType = SectionType.READING,
) {
    abstract fun getQuestions(): List<Question>
    
}

@TypeAlias("reading_parts")
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

@TypeAlias("parts.passages")
class Passage(
    var indicator: String?,
    var paragraph: String
)

@TypeAlias("listening_parts")
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


@TypeAlias("writing_parts")
class WritingPart(
    order: Int,
    var question: WritingQuestion
) : Part(order, SectionType.WRITING) {
    override fun getQuestions(): List<Question> {
        return listOf(question)
    }
}


@TypeAlias("speaking_parts")
class SpeakingPart(
    order: Int,
    var questionList: List<SpeakingQuestion>,
    var focus: String?
) : Part(order, SectionType.SPEAKING) {
    override fun getQuestions(): List<Question> {
        return questionList
    }
}
