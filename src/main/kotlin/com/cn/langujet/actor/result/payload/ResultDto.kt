package com.cn.langujet.actor.result.payload

import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.result.model.Result

data class ResultDto(
    var id: String,
    var examSessionId: String,
    val examType: ExamType,
    var score: Double?,
    var recommendation: String?,
) {
    constructor(result: Result) : this(
        result.id ?: "",
        result.examSessionId,
        result.examType,
        result.score,
        result.recommendation,
    )
}