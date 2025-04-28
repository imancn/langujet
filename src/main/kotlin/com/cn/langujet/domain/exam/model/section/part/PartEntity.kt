package com.cn.langujet.domain.exam.model.section.part

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.domain.exam.model.enums.SectionType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@Schema(subTypes = [ReadingPartEntity::class, ListeningPartEntity::class, WritingPartEntity::class, SpeakingPartEntity::class])
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ReadingPartEntity::class, name = "READING"),
    JsonSubTypes.Type(value = ListeningPartEntity::class, name = "LISTENING"),
    JsonSubTypes.Type(value = WritingPartEntity::class, name = "WRITING"),
    JsonSubTypes.Type(value = SpeakingPartEntity::class, name = "SPEAKING")
)
@TypeAlias("parts")
@Document(collection = "parts")
@CompoundIndexes(
    CompoundIndex(
        name = "unique_parts_index",
        def = "{'examId': -1, 'sectionId': 1, 'order': 1}",
        unique = true
    )
)
abstract class PartEntity(
    id: Long?,
    var examId: Long,
    var sectionId: Long,
    var order: Int,
    var type: SectionType,
) : HistoricalEntity(id = id)

@TypeAlias("reading_parts")
@Document(collection = "parts")
class ReadingPartEntity(
    id: Long?,
    examId: Long,
    sectionId: Long,
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
    examId: Long,
    sectionId: Long,
    order: Int,
    var audioId: Long,
    var time: Long
) : PartEntity(id = id, examId = examId, sectionId = sectionId, order = order, type = SectionType.LISTENING)


@TypeAlias("writing_parts")
@Document(collection = "parts")
class WritingPartEntity(
    id: Long?,
    examId: Long,
    sectionId: Long,
    order: Int,
) : PartEntity(id = id, examId = examId, sectionId = sectionId, order = order, type = SectionType.WRITING)


@TypeAlias("speaking_parts")
@Document(collection = "parts")
class SpeakingPartEntity(
    id: Long?,
    examId: Long,
    sectionId: Long,
    order: Int,
    var focus: String?
) : PartEntity(id = id, examId = examId, sectionId = sectionId, order = order, type = SectionType.SPEAKING)