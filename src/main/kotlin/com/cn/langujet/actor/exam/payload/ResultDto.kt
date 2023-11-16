package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.Result
import com.cn.langujet.domain.exam.model.SectionResult
import com.cn.langujet.domain.exam.model.SectionType

data class ResultDto(
    var examSessionId: String,
    var score: Int,
    var recommendation: String,
    var sectionResults: List<SectionResultDto>
) {
    constructor(result: Result) : this(
        result.examSessionId,
        result.score,
        result.recommendation,
        result.sectionResults.map { SectionResultDto(it) }
    )
}

data class SectionResultDto(
    var sectionId: String,
    var sectionType: SectionType,
    var score: Int,
    var recommendation: String
) {
    constructor(sectionResult: SectionResult) : this(
        sectionResult.sectionId,
        sectionResult.sectionType,
        sectionResult.score,
        sectionResult.recommendation
    )
}
