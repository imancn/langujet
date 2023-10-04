package com.cn.langujet.domain.exam.model

import com.cn.langujet.domain.exam.model.question.Question
import com.cn.langujet.domain.exam.model.question.SpeakingQuestion
import com.cn.langujet.domain.exam.model.question.WritingQuestion

sealed class Part(
    var index: Int,
    var issueType: SectionType,
)

class ReadingPart(
    index: Int,
    var passage: String,
    var questionList: List<Question>
) : Part(index, SectionType.READING)

class ListeningPart(
    index: Int,
    var audioAddress: String,
    var questionList: List<Question>
) : Part(index, SectionType.LISTENING)

class WritingPart(
    index: Int,
    var question: WritingQuestion
) : Part(index, SectionType.WRITING)

class SpeakingPart(
    index: Int,
    var questionList: List<SpeakingQuestion>
) : Part(index, SectionType.SPEAKING)
