package com.cn.langujet.domain.correction.repository

import com.cn.langujet.domain.correction.model.CorrectionEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import org.springframework.data.mongodb.repository.MongoRepository

interface CorrectionRepository: MongoRepository<CorrectionEntity, String> {
    fun findByCorrectorTypeAndStatusOrderByCreatedDateAsc(correctorType: CorrectorType, status: CorrectionStatus): List<CorrectionEntity>
    fun findAllByExamSessionId(examSessionId: String): List<CorrectionEntity>
    fun findByStatusAndCorrectorUserId(status: CorrectionStatus, userId: String): List<CorrectionEntity>
}
