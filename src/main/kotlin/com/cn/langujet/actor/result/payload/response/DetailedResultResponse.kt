package com.cn.langujet.actor.result.payload.response

import com.cn.langujet.domain.exam.model.enums.ExamType
import com.cn.langujet.domain.result.model.ResultEntity
import com.cn.langujet.domain.result.model.SectionResultEntity

data class DetailedResultResponse(
    var examSessionId: String,
    val examType: ExamType,
    val sectionResults: List<SectionResultResponse>,
    var score: Double?,
    var recommendation: String?,
) {
    constructor(result: ResultEntity, sectionResults: List<SectionResultEntity>) : this(
        result.examSessionId,
        result.examType,
        sectionResults.map { SectionResultResponse(it) },
        result.score,
        result.recommendation,
    )
}