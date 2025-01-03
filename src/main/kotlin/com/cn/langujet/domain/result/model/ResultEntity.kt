package com.cn.langujet.domain.result.model

import com.cn.langujet.application.shared.HistoricalEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamMode
import com.cn.langujet.domain.exam.model.ExamType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "results")
@TypeAlias("results")
data class ResultEntity(
    @Id var id: String?,
    var examSessionId: String,
    var examType: ExamType,
    var examMode: ExamMode,
    var correctorType: CorrectorType,
    var correctorUserId: String?,
    var status: CorrectionStatus,
    var score: Double?,
    var recommendation: String?
): HistoricalEntity()

