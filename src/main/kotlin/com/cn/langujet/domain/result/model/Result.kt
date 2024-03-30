package com.cn.langujet.domain.result.model

import com.cn.langujet.domain.exam.model.ExamType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "results")
data class Result(
    @Id var id: String?,
    var examSessionId: String,
    var examType: ExamType,
    var score: Double?,
    var recommendation: String?,
)

