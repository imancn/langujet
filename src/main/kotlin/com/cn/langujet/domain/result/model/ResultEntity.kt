package com.cn.langujet.domain.result.model

import com.cn.langujet.application.shared.HistoricalEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "results")
@TypeAlias("results")
data class ResultEntity(
    @Id var id: String?,
    @Indexed(name = "unique_results_index", unique = true, direction = IndexDirection.DESCENDING)
    var examSessionId: String,
    var examType: ExamType,
    var examMode: ExamMode,
    var correctorType: CorrectorType,
    @Indexed(name = "corrector_user_id_index", unique = false)
    var correctorUserId: String?,
    @Indexed(name = "status_index", unique = false)
    var status: CorrectionStatus,
    var score: Double?,
    var recommendation: String?
): HistoricalEntity()

