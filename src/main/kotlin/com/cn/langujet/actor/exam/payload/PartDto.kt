package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.*
import io.swagger.v3.oas.annotations.media.Schema

@Schema(subTypes = [ReadingPartDTO::class, ListeningPartDTO::class, WritingPartDTO::class, SpeakingPartDTO::class])
sealed class PartDTO(
    open val index: Int,
    open val type: SectionType
) {
    companion object {
        fun from(part: Part): PartDTO {
            return when (part) {
                is ReadingPart -> ReadingPartDTO(part)
                is ListeningPart -> ListeningPartDTO(part)
                is WritingPart -> WritingPartDTO(part)
                is SpeakingPart -> SpeakingPartDTO(part)
                else -> throw IllegalArgumentException("Unknown Part type")
            }
        }
    }
}

data class ReadingPartDTO(
    override val index: Int,
    val passage: String,
    val questionList: List<QuestionDTO>
) : PartDTO(index, SectionType.READING) {
    constructor(part: ReadingPart) : this(
        part.index,
        part.passage,
        part.questionList.map { QuestionDTO.from(it) }
    )
}

data class ListeningPartDTO(
    override val index: Int,
    val audioAddress: String,
    val questionList: List<QuestionDTO>
) : PartDTO(index, SectionType.LISTENING) {
    constructor(part: ListeningPart) : this(
        part.index,
        part.audioAddress,
        part.questionList.map { QuestionDTO.from(it) }
    )
}

data class WritingPartDTO(
    override val index: Int,
    val question: WritingQuestionDTO
) : PartDTO(index, SectionType.WRITING) {
    constructor(part: WritingPart) : this(
        part.index,
        WritingQuestionDTO(part.question.index, part.question.header, part.question.time, part.question.content)
    )
}

data class SpeakingPartDTO(
    override val index: Int,
    val questionList: List<SpeakingQuestionDTO>
) : PartDTO(index, SectionType.SPEAKING) {
    constructor(part: SpeakingPart) : this(
        part.index,
        part.questionList.map { SpeakingQuestionDTO(it.index, it.header, it.time) }
    )
}