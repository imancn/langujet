package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.model.section.part.*
import com.cn.langujet.domain.exam.model.section.part.questions.QuestionEntity
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema

@Schema(subTypes = [ReadingPartDTO::class, ListeningPartDTO::class, WritingPartDTO::class, SpeakingPartDTO::class])
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ReadingPartDTO::class, name = "READING"),
    JsonSubTypes.Type(value = ListeningPartDTO::class, name = "LISTENING"),
    JsonSubTypes.Type(value = WritingPartDTO::class, name = "WRITING"),
    JsonSubTypes.Type(value = SpeakingPartDTO::class, name = "SPEAKING")
)
sealed class PartDTO(
    open var partOrder: Int,
    open var type: SectionType
) {
    inline fun <reified T : PartEntity> toPart(examId: String, sectionId: String): T {
        val part = when (this) {
            is ReadingPartDTO -> ReadingPartEntity(
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
            
            is ListeningPartDTO -> ListeningPartEntity(
                id = null,
                examId = examId,
                sectionId = sectionId,
                order = this.partOrder,
                audioId = this.audioId,
                time = this.time
            )
            
            is WritingPartDTO -> WritingPartEntity(
                id = null,
                examId = examId,
                sectionId = sectionId,
                order = this.partOrder
            )
            
            is SpeakingPartDTO -> SpeakingPartEntity(
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
        inline fun <reified T : PartDTO> from(part: PartEntity, questions: List<QuestionEntity>): T {
            val partDTO = when (part) {
                is ReadingPartEntity -> ReadingPartDTO(part = part, questions = questions)
                is ListeningPartEntity -> ListeningPartDTO(part = part, questions = questions)
                is WritingPartEntity -> WritingPartDTO(part = part, question = questions.first())
                is SpeakingPartEntity -> SpeakingPartDTO(part = part, questions = questions)
                else -> throw IllegalArgumentException("Unknown Part type")
            }
            if (partDTO !is T) throw TypeCastException("The type of question does not match the reified type.")
            return partDTO
        }
    }
    
    abstract fun getQuestions(): List<QuestionDTO>
}

data class ReadingPartDTO(
    override var partOrder: Int,
    var passageHeader: String?,
    var passage: List<PassageDTO>,
    var questionList: List<QuestionDTO>
) : PartDTO(partOrder = partOrder, SectionType.READING) {
    constructor(part: ReadingPartEntity, questions: List<QuestionEntity>) : this(
        partOrder = part.order,
        passageHeader = part.passageHeader,
        passage = part.passage.map { PassageDTO(it) },
        questionList = questions.map { QuestionDTO.from(it) },
    )
    
    override fun getQuestions(): List<QuestionDTO> {
        return questionList
    }
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

data class ListeningPartDTO(
    override var partOrder: Int,
    val audioId: String,
    var questionList: List<QuestionDTO>,
    var time: Long
) : PartDTO(partOrder = partOrder, SectionType.LISTENING) {
    constructor(part: ListeningPartEntity, questions: List<QuestionEntity>) : this(
        partOrder = part.order,
        audioId = part.audioId,
        questionList = questions.map { QuestionDTO.from(it) },
        time = part.time
    )
    
    override fun getQuestions(): List<QuestionDTO> {
        return questionList
    }
}

data class WritingPartDTO(
    override var partOrder: Int,
    var question: WritingQuestionDTO
) : PartDTO(partOrder = partOrder, SectionType.WRITING) {
    constructor(part: WritingPartEntity, question: QuestionEntity) : this(
        partOrder = part.order,
        question = QuestionDTO.from(question),
    )
    
    override fun getQuestions(): List<QuestionDTO> {
        return listOf(question)
    }
}

data class SpeakingPartDTO(
    override var partOrder: Int,
    var questionList: List<SpeakingQuestionDTO>,
    var focus: String? = null
) : PartDTO(partOrder = partOrder, SectionType.SPEAKING) {
    constructor(part: SpeakingPartEntity, questions: List<QuestionEntity>) : this(
        partOrder = part.order,
        questionList = questions.map { QuestionDTO.from(it) },
        focus = part.focus
    )
    
    override fun getQuestions(): List<QuestionDTO> {
        return questionList
    }
}