package com.cn.langujet.domain.result.repository

import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.result.model.SectionResultEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface SectionResultRepository : MongoRepository<SectionResultEntity, Long> {
    fun findByStatusAndResultId(status: CorrectionStatus, resultId: Long): List<SectionResultEntity>
    fun findByResultId(resultId: Long): List<SectionResultEntity>
}