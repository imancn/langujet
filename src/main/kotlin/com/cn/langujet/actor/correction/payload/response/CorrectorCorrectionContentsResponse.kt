package com.cn.langujet.actor.correction.payload.response

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import com.cn.langujet.domain.exam.model.enums.SectionType

data class CorrectorCorrectionExamSessionContentResponse(
    val examSessionId: Long,
    val section: CorrectorCorrectionSectionResponse,
)

data class CorrectorCorrectionSectionResponse (
    val header: String,
    val sectionOrder: Int,
    val sectionType: SectionType,
    val parts: List<CorrectorCorrectionPartResponse>,
)

sealed class CorrectorCorrectionPartResponse(
    open val partOrder: Int,
    open val partType: SectionType
)

data class WritingCorrectorCorrectionPartResponse(
    override val partOrder: Int,
    val question: WritingCorrectorCorrectionQuestionResponse,
    val answer: TextCorrectorCorrectionAnswerResponse?
) : CorrectorCorrectionPartResponse(partOrder, SectionType.WRITING)

data class SpeakingCorrectorCorrectionPartResponse(
    override val partOrder: Int,
    val questionAnswerList: List<SpeakingCorrectorCorrectionQuestionAnswerResponse>,
) : CorrectorCorrectionPartResponse(partOrder, SectionType.SPEAKING)

data class SpeakingCorrectorCorrectionQuestionAnswerResponse(
    val questionOrder: Int,
    val question: SpeakingCorrectorCorrectionQuestionResponse,
    val answer: VoiceCorrectorCorrectionAnswerResponse?
)

sealed class CorrectorCorrectionQuestionResponse(
    open val header: String,
    val questionType: QuestionType,
    val answerType: AnswerType
)

data class SpeakingCorrectorCorrectionQuestionResponse(
    override val header: String,
    val audioUrl: String?,
) : CorrectorCorrectionQuestionResponse(header, QuestionType.SPEAKING, AnswerType.VOICE)

data class WritingCorrectorCorrectionQuestionResponse(
    override val header: String,
    val content: String?,
) : CorrectorCorrectionQuestionResponse(header, QuestionType.WRITING, AnswerType.TEXT)

sealed class CorrectorCorrectionAnswerResponse(
    open val answerType: AnswerType
)

data class VoiceCorrectorCorrectionAnswerResponse(
    val voiceLink: String
) : CorrectorCorrectionAnswerResponse(AnswerType.VOICE)

data class TextCorrectorCorrectionAnswerResponse(
    val text: String
) : CorrectorCorrectionAnswerResponse(AnswerType.TEXT)