package com.cn.langujet.domain.exam.model

import com.cn.langujet.domain.exam.model.question.Question
import com.cn.langujet.domain.exam.model.question.SpeakingQuestion
import com.cn.langujet.domain.exam.model.question.WritingQuestion

sealed class Part(
    var index: Int,
    var type: SectionType,
)

class ReadingPart(
    index: Int,
    var passage: List<Passage>,
    var questionList: List<Question>
) : Part(index, SectionType.READING)

class Passage(
    var indicator: String?,
    var paragraph: String
)

class ListeningPart(
    index: Int,
    var audioId: String,
    var questionList: List<Question>
) : Part(index, SectionType.LISTENING)

class WritingPart(
    index: Int,
    var question: WritingQuestion
) : Part(index, SectionType.WRITING)

class SpeakingPart(
    index: Int,
    var questionList: List<SpeakingQuestion>,
    var focus: String?
) : Part(index, SectionType.SPEAKING)
