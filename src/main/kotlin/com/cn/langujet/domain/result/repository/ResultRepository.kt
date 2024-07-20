package com.cn.langujet.domain.result.repository

import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.result.model.Result
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ResultRepository : MongoRepository<Result, String> {
    fun findByExamSessionId(examSessionId: String): Optional<Result>
    fun findByCorrectorTypeAndStatusOrderByCreatedDateAsc(
        correctorType: CorrectorType,
        correctionStatus: CorrectionStatus
    ) : List<Result>
    
    fun findByStatusAndCorrectorUserIdOrderByCreatedDateAsc(
        correctionStatus: CorrectionStatus,
        correctorId: String
    ): List<Result>
}