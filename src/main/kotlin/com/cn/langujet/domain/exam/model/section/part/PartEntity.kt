package com.cn.langujet.domain.exam.model.section.part

import com.cn.langujet.application.arch.mongo.models.SequentialEntity
import com.cn.langujet.domain.exam.model.enums.SectionType
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@TypeAlias("parts")
@Document(collection = "parts")
sealed class PartEntity(
    id: Long?,
    var examId: String,
    var sectionId: String,
    var order: Int,
    var type: SectionType,
) : SequentialEntity(id = id)

@TypeAlias("reading_parts")
@Document(collection = "parts")
class ReadingPartEntity(
    id: Long?,
    examId: String,
    sectionId: String,
    order: Int,
    var passageHeader: String?,
    var passage: List<ReadingPassage>,
) : PartEntity(id = id, examId = examId, sectionId = sectionId, order = order, type = SectionType.READING)

@TypeAlias("reading_parts.passages")
class ReadingPassage(
    var indicator: String?,
    var paragraph: String
)

@TypeAlias("listening_parts")
@Document(collection = "parts")
class ListeningPartEntity(
    id: Long?,
    examId: String,
    sectionId: String,
    order: Int,
    var audioId: String,
    var time: Long
) : PartEntity(id = id, examId = examId, sectionId = sectionId, order = order, type = SectionType.LISTENING)


@TypeAlias("writing_parts")
@Document(collection = "parts")
class WritingPartEntity(
    id: Long?,
    examId: String,
    sectionId: String,
    order: Int,
) : PartEntity(id = id, examId = examId, sectionId = sectionId, order = order, type = SectionType.WRITING)


@TypeAlias("speaking_parts")
@Document(collection = "parts")
class SpeakingPartEntity(
    id: Long?,
    examId: String,
    sectionId: String,
    order: Int,
    var focus: String?
) : PartEntity(id = id, examId = examId, sectionId = sectionId, order = order, type = SectionType.SPEAKING)