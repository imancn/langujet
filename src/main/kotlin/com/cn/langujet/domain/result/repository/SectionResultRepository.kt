package com.cn.langujet.domain.result.repository

import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.result.model.SectionResultEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface SectionResultRepository : MongoRepository<SectionResultEntity, String> {
    fun findByStatusAndResultId(status: CorrectionStatus, resultId: String): List<SectionResultEntity>
    fun findByResultId(resultId: String): List<SectionResultEntity>
}