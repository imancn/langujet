package com.cn.langujet.domain.exam.model

import com.cn.langujet.domain.exam.model.question.Question
import com.cn.langujet.domain.exam.model.question.SpeakingQuestion
import com.cn.langujet.domain.exam.model.question.WritingQuestion

sealed class Part(
    var order: Int,
    var type: SectionType,
)

class ReadingPart(
    order: Int,
    var passage: List<Passage>,
    var questionList: List<Question>
) : Part(order, SectionType.READING)

class Passage(
    var indicator: String?,
    var paragraph: String
)

class ListeningPart(
    order: Int,
    var audioId: String,
    var questionList: List<Question>,
    var time: Long
) : Part(order, SectionType.LISTENING)

class WritingPart(
    order: Int,
    var question: WritingQuestion
) : Part(order, SectionType.WRITING)

class SpeakingPart(
    order: Int,
    var questionList: List<SpeakingQuestion>,
    var focus: String?
) : Part(order, SectionType.SPEAKING)
