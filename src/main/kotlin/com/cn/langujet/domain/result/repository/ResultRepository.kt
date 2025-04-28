package com.cn.langujet.domain.result.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.result.model.ResultEntity
import java.util.*

interface ResultRepository : HistoricalMongoRepository<ResultEntity> {
    fun findByExamSessionId(examSessionId: Long): Optional<ResultEntity>
    fun findByCorrectorTypeAndStatusOrderByCreatedAtAsc(
        correctorType: CorrectorType,
        correctionStatus: CorrectionStatus
    ) : List<ResultEntity>
    
    fun findByStatusAndCorrectorUserIdOrderByCreatedAtAsc(
        correctionStatus: CorrectionStatus,
        correctorId: Long
    ): List<ResultEntity>
}