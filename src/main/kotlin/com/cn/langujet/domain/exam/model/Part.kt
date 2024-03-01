package com.cn.langujet.domain.exam.model

import com.cn.langujet.domain.exam.model.question.Question
import com.cn.langujet.domain.exam.model.question.SpeakingQuestion
import com.cn.langujet.domain.exam.model.question.WritingQuestion

sealed class Part(
    var id: Int,
    var type: SectionType,
)

class ReadingPart(
    id: Int,
    var passage: List<Passage>,
    var questionList: List<Question>
) : Part(id, SectionType.READING)

class Passage(
    var indicator: String?,
    var paragraph: String
)

class ListeningPart(
    id: Int,
    var audioId: String,
    var questionList: List<Question>
) : Part(id, SectionType.LISTENING)

class WritingPart(
    id: Int,
    var question: WritingQuestion
) : Part(id, SectionType.WRITING)

class SpeakingPart(
    id: Int,
    var questionList: List<SpeakingQuestion>,
    var focus: String?
) : Part(id, SectionType.SPEAKING)
