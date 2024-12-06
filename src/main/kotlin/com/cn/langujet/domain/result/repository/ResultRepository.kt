package com.cn.langujet.domain.result.repository

import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.result.model.ResultEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ResultRepository : MongoRepository<ResultEntity, String> {
    fun findByExamSessionId(examSessionId: String): Optional<ResultEntity>
    fun findByCorrectorTypeAndStatusOrderByCreatedAtAsc(
        correctorType: CorrectorType,
        correctionStatus: CorrectionStatus
    ) : List<ResultEntity>
    
    fun findByStatusAndCorrectorUserIdOrderByCreatedAtAsc(
        correctionStatus: CorrectionStatus,
        correctorId: String
    ): List<ResultEntity>
}