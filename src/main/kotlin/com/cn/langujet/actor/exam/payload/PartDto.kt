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
    open val partId: Int? = null,
    open val type: SectionType? = null
) {
    inline fun <reified T : Part> toPart(): T {
        val part = when (this) {
            is ReadingPartDTO -> ReadingPart(
                this.partId!!,
                this.passage!!.map {
                    Passage(
                        it.indicator,
                        it.paragraph!!
                    )
                },
                this.questionList?.map { it.toQuestion() }!!)

            is ListeningPartDTO -> ListeningPart(
                this.partId!!,
                this.audioId!!,
                this.questionList?.map { it.toQuestion() }!!
            )

            is WritingPartDTO -> WritingPart(
                this.partId!!,
                WritingQuestion(
                    this.question?.questionId!!,
                    this.question.header!!,
                    this.question.time!!,
                    this.question.content
                )
            )

            is SpeakingPartDTO -> SpeakingPart(
                this.partId!!,
                this.questionList!!.map { SpeakingQuestion(it.questionId!!, it.header!!, it.time!!) },
                this.focus
            )

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
    override val partId: Int? = null,
    val passage: List<PassageDTO>? = null,
    val questionList: List<QuestionDTO>? = null
) : PartDTO(partId, SectionType.READING) {
    constructor(part: ReadingPart) : this(
        part.id,
        part.passage.map { PassageDTO(it) },
        part.questionList.map { QuestionDTO.from(it) }
    )
}

data class PassageDTO(
    var indicator: String? = null,
    var paragraph: String? = null
) {
    constructor(passage: Passage) : this(
        passage.indicator,
        passage.paragraph
    )
}

data class ListeningPartDTO(
    override val partId: Int? = null,
    val audioId: String? = null,
    val questionList: List<QuestionDTO>? = null
) : PartDTO(partId, SectionType.LISTENING) {
    constructor(part: ListeningPart) : this(
        part.id,
        part.audioId,
        part.questionList.map { QuestionDTO.from(it) }
    )
}

data class WritingPartDTO(
    override val partId: Int? = null,
    val question: WritingQuestionDTO? = null
) : PartDTO(partId, SectionType.WRITING) {
    constructor(part: WritingPart) : this(
        part.id,
        WritingQuestionDTO(part.question.id, part.question.header, part.question.time, part.question.content)
    )
}

data class SpeakingPartDTO(
    override val partId: Int? = null,
    val questionList: List<SpeakingQuestionDTO>? = null,
    val focus: String? = null
) : PartDTO(partId, SectionType.SPEAKING) {
    constructor(part: SpeakingPart) : this(
        part.id,
        part.questionList.map { SpeakingQuestionDTO(it.id, it.header, it.time) },
        part.focus
    )
}