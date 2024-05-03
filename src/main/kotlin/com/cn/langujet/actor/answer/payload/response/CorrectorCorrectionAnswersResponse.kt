package com.cn.langujet.actor.answer.payload.response

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType

class CorrectorCorrectionAnswersResponse(
    val examSessionId: String,
    val sectionOrder: Int,
    val answers: List<CorrectorAnswerResponse>
)

sealed class CorrectorAnswerResponse(
    open val partOrder: Int,
    open val questionOrder: Int,
    open val answerType: AnswerType
)

data class VoiceCorrectorAnswerResponse(
    override val partOrder: Int,
    override val questionOrder: Int,
    val voiceLink: String,
) : CorrectorAnswerResponse(partOrder, questionOrder, AnswerType.VOICE)

data class TextCorrectorAnswerResponse(
    override val partOrder: Int,
    override val questionOrder: Int,
    val text: String,
) : CorrectorAnswerResponse(partOrder, questionOrder, AnswerType.TEXT)

data class TextIssuesCorrectorAnswerResponse(
    override val partOrder: Int,
    override val questionOrder: Int,
    val issues: List<String?>,
) : CorrectorAnswerResponse(partOrder, questionOrder, AnswerType.TEXT_ISSUES)

data class TrueFalseCorrectorAnswerResponse(
    override val partOrder: Int,
    override val questionOrder: Int,
    val issues: List<TrueFalseAnswerType?>,
) : CorrectorAnswerResponse(partOrder, questionOrder, AnswerType.TRUE_FALSE)

data class MultipleChoiceCorrectorAnswerResponse(
    override val partOrder: Int,
    override val questionOrder: Int,
    val issues: List<MultipleChoiceIssueCorrectorAnswerResponse?>,
) : CorrectorAnswerResponse(partOrder, questionOrder, AnswerType.MULTIPLE_CHOICE)

data class MultipleChoiceIssueCorrectorAnswerResponse(
    var issueOrder: Int,
    var options: List<String?>
)