package com.cn.langujet.actor.result.payload.response

import com.cn.langujet.domain.exam.model.enums.ExamType
import com.cn.langujet.domain.result.model.ResultEntity
import com.cn.langujet.domain.result.model.SectionResultEntity

data class DetailedResultResponse(
    var examSessionId: Long,
    val examType: ExamType,
    val sectionResults: List<SectionResultResponse>,
    var score: Double?,
    var recommendation: String?,
) {
    constructor(result: ResultEntity, sectionResults: List<Pair<SectionResultEntity, String?>>) : this(
        result.examSessionId,
        result.examType,
        sectionResults.map { SectionResultResponse(it.first, it.second) },
        result.score,
        result.recommendation,
    )
}