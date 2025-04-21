package com.cn.langujet.domain.result.repository

import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.result.model.ResultEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ResultRepository : MongoRepository<ResultEntity, Long> {
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