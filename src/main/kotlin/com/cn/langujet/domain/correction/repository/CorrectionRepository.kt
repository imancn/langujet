package com.cn.langujet.domain.correction.repository

import com.cn.langujet.domain.correction.model.CorrectionEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface CorrectionRepository: MongoRepository<CorrectionEntity, String> {
    fun findByTypeAndStatusOrderByCreatedDateAsc(type: CorrectionType, status: CorrectionStatus, pageRequest: PageRequest): Page<CorrectionEntity>
    fun findAllByExamSessionId(examSessionId: String): List<CorrectionEntity>
    fun findAllByExamSessionIdAndSectionOrder(examSessionId: String, sectionOrder: Int): Optional<CorrectionEntity>
}
