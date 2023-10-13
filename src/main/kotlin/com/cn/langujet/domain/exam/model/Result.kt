package com.cn.langujet.domain.exam.model

import com.cn.langujet.actor.exam.payload.ResultDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "results")
data class Result(
    @Id var id: String?,
    var examSessionId: String,
    var score: Int,
    var recommendation: String,
    var sectionResults: List<SectionResult>
) {
    constructor(resultDto: ResultDto) : this(
        resultDto.id,
        resultDto.examSessionId,
        resultDto.score,
        resultDto.recommendation,
        resultDto.sectionResults.map { SectionResult(it) }
    )
}

