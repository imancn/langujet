package com.cn.langujet.actor.result.payload.response

import com.cn.langujet.domain.exam.model.SectionType
import com.cn.langujet.domain.result.model.SectionResult

data class SectionResultResponse(
    var sectionOrder: Int,
    var sectionType: SectionType,
    var correctIssuesCount: Int?,
    var score: Double?,
    var recommendation: String?
) {
    constructor(sectionResult: SectionResult) : this(
        sectionResult.sectionOrder,
        sectionResult.sectionType,
        sectionResult.correctIssuesCount,
        sectionResult.score,
        sectionResult.recommendation
    )
}