package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.*
import com.cn.langujet.domain.exam.model.question.SpeakingQuestion
import com.cn.langujet.domain.exam.model.question.WritingQuestion
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
    open val index: Int? = null,
    open val type: SectionType? = null
) {
    inline fun <reified T : Part> toPart(): T {
        val part = when (this) {
            is ReadingPartDTO -> ReadingPart(this.index!!, this.passage!!, this.questionList?.map { it.toQuestion() }!!)

            is ListeningPartDTO -> ListeningPart(
                this.index!!,
                this.audioId!!,
                this.questionList?.map { it.toQuestion() }!!
            )

            is WritingPartDTO -> WritingPart(
                this.index!!,
                WritingQuestion(
                    this.question?.index!!,
                    this.question.header!!,
                    this.question.time!!,
                    this.question.content
                )
            )

            is SpeakingPartDTO -> SpeakingPart(
                this.index!!,
                this.questionList!!.map { SpeakingQuestion(it.index!!, it.header!!, it.time!!) })

            else -> throw IllegalArgumentException("Unknown PartDTO type")
        }
        if (part !is T) throw TypeCastException("The type of question does not match the reified type.")
        return part
    }

    companion object {
        inline fun <reified T : PartDTO> from(part: Part): T {
            val partDTO = when (part) {
                is ReadingPart -> ReadingPartDTO(part)
                is ListeningPart -> ListeningPartDTO(part)
                is WritingPart -> WritingPartDTO(part)
                is SpeakingPart -> SpeakingPartDTO(part)
                else -> throw IllegalArgumentException("Unknown Part type")
            }
            if (partDTO !is T) throw TypeCastException("The type of question does not match the reified type.")
            return partDTO
        }
    }
}

data class ReadingPartDTO(
    override val index: Int? = null,
    val passage: String? = null,
    val questionList: List<QuestionDTO>? = null
) : PartDTO(index, SectionType.READING) {
    constructor(part: ReadingPart) : this(
        part.index,
        part.passage,
        part.questionList.map { QuestionDTO.from(it) }
    )
}

data class ListeningPartDTO(
    override val index: Int? = null,
    val audioId: String? = null,
    val questionList: List<QuestionDTO>? = null
) : PartDTO(index, SectionType.LISTENING) {
    constructor(part: ListeningPart) : this(
        part.index,
        part.audioId,
        part.questionList.map { QuestionDTO.from(it) }
    )
}

data class WritingPartDTO(
    override val index: Int? = null,
    val question: WritingQuestionDTO? = null
) : PartDTO(index, SectionType.WRITING) {
    constructor(part: WritingPart) : this(
        part.index,
        WritingQuestionDTO(part.question.index, part.question.header, part.question.time, part.question.content)
    )
}

data class SpeakingPartDTO(
    override val index: Int? = null,
    val questionList: List<SpeakingQuestionDTO>? = null
) : PartDTO(index, SectionType.SPEAKING) {
    constructor(part: SpeakingPart) : this(
        part.index,
        part.questionList.map { SpeakingQuestionDTO(it.index, it.header, it.time) }
    )
}