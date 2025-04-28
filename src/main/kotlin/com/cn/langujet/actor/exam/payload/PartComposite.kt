package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.model.section.part.*
import com.cn.langujet.domain.exam.model.section.part.questions.QuestionEntity
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema

@Schema(subTypes = [ReadingPartComposite::class, ListeningPartComposite::class, WritingPartComposite::class, SpeakingPartComposite::class])
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ReadingPartComposite::class, name = "READING"),
    JsonSubTypes.Type(value = ListeningPartComposite::class, name = "LISTENING"),
    JsonSubTypes.Type(value = WritingPartComposite::class, name = "WRITING"),
    JsonSubTypes.Type(value = SpeakingPartComposite::class, name = "SPEAKING")
)
sealed class PartComposite(
    id: Long? = null,
    open var partOrder: Int,
    open var type: SectionType,
    open var questions: List<QuestionDTO>
) {
    inline fun <reified T : PartEntity> toPart(examId: Long, sectionId: Long): T {
        val part = when (this) {
            is ReadingPartComposite -> ReadingPartEntity(
                id = null,
                examId = examId,
                sectionId = sectionId,
                order = this.partOrder,
                passageHeader = this.passageHeader,
                passage = this.passage.map {
                    ReadingPassage(
                        indicator = it.indicator,
                        paragraph = it.paragraph
                    )
                }
            )
            
            is ListeningPartComposite -> ListeningPartEntity(
                id = null,
                examId = examId,
                sectionId = sectionId,
                order = this.partOrder,
                audioId = this.audioId,
                time = this.time
            )
            
            is WritingPartComposite -> WritingPartEntity(
                id = null,
                examId = examId,
                sectionId = sectionId,
                order = this.partOrder
            )
            
            is SpeakingPartComposite -> SpeakingPartEntity(
                id = null,
                examId = examId,
                sectionId = sectionId,
                order = this.partOrder,
                focus = this.focus
            )

            else -> throw IllegalArgumentException("Unknown PartDTO type")
        }
        if (part !is T) throw TypeCastException("The type of question does not match the reified type.")
        return part
    }

    companion object {
        inline fun <reified T : PartComposite> from(part: PartEntity, questions: List<QuestionEntity>): T {
            val partDTO = when (part) {
                is ReadingPartEntity -> ReadingPartComposite(part, questions = questions)
                is ListeningPartEntity -> ListeningPartComposite(part, questions = questions)
                is WritingPartEntity -> WritingPartComposite(part, questions = questions)
                is SpeakingPartEntity -> SpeakingPartComposite(part, questions = questions)
                else -> throw IllegalArgumentException("Unknown Part type")
            }
            if (partDTO !is T) throw TypeCastException("The type of question does not match the reified type.")
            return partDTO
        }
    }
}

class ReadingPartComposite(
    id: Long? = null,
    override var partOrder: Int,
    override var questions: List<QuestionDTO>,
    var passageHeader: String?,
    var passage: List<PassageDTO>,
) : PartComposite(id = id, partOrder = partOrder, questions = questions, type = SectionType.READING) {
    constructor(part: ReadingPartEntity, questions: List<QuestionEntity>) : this(
        partOrder = part.order,
        passageHeader = part.passageHeader,
        passage = part.passage.map { PassageDTO(it) },
        questions = questions.map { QuestionDTO.from(it) }
    )
}

class ListeningPartComposite(
    id: Long? = null,
    override var partOrder: Int,
    override var questions: List<QuestionDTO>,
    val audioId: Long,
    var time: Long
) : PartComposite(id = id, partOrder = partOrder, questions = questions, type = SectionType.LISTENING) {
    constructor(part: ListeningPartEntity, questions: List<QuestionEntity>) : this(
        partOrder = part.order,
        audioId = part.audioId,
        time = part.time,
        questions = questions.map { QuestionDTO.from(it) }
    )
}

class WritingPartComposite(
    id: Long? = null,
    override var partOrder: Int,
    override var questions: List<QuestionDTO>
) : PartComposite(id = id, partOrder = partOrder, questions = questions, type = SectionType.WRITING) {
    constructor(part: WritingPartEntity, questions: List<QuestionEntity>) : this(
        partOrder = part.order, questions = questions.map { QuestionDTO.from(it) }
    )
}

class SpeakingPartComposite(
    id: Long? = null,
    override var partOrder: Int,
    override var questions: List<QuestionDTO>,
    var focus: String? = null
) : PartComposite(id = id, partOrder = partOrder, questions = questions, type = SectionType.SPEAKING) {
    constructor(part: SpeakingPartEntity, questions: List<QuestionEntity>) : this(
        partOrder = part.order,
        focus = part.focus,
        questions = questions.map { QuestionDTO.from(it) }
    )
}

data class PassageDTO(
    var indicator: String?,
    var paragraph: String
) {
    constructor(readingPassage: ReadingPassage) : this(
        readingPassage.indicator,
        readingPassage.paragraph
    )
}