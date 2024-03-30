package com.cn.langujet.actor.result.payload

import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.result.model.Result
import com.cn.langujet.domain.result.model.SectionResult

data class DetailedResultResponse(
    var examSessionId: String,
    val examType: ExamType,
    val sectionResults: List<SectionResultResponse>,
    var score: Double?,
    var recommendation: String?,
) {
    constructor(result: Result, sectionResults: List<SectionResult>) : this(
        result.examSessionId,
        result.examType,
        sectionResults.map { SectionResultResponse(it) },
        result.score,
        result.recommendation,
    )
}