package com.cn.langujet.actor.result.payload.response

import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.result.model.SectionResultEntity

data class SectionResultResponse(
    var sectionOrder: Int,
    var sectionType: SectionType,
    var correctIssuesCount: Int?,
    var score: Double?,
    var recommendation: String?,
    var attachmentUrl: String?
) {
    constructor(sectionResult: SectionResultEntity, attachmentUrl: String? = null) : this(
        sectionResult.sectionOrder,
        sectionResult.sectionType,
        sectionResult.correctIssuesCount,
        sectionResult.score,
        sectionResult.recommendation,
        attachmentUrl
    )
}