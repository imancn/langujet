package com.cn.langujet.domain.result.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.result.model.SectionResultEntity

interface SectionResultRepository : HistoricalMongoRepository<SectionResultEntity> {
    fun findByStatusAndResultId(status: CorrectionStatus, resultId: Long): List<SectionResultEntity>
    fun findByResultId(resultId: Long): List<SectionResultEntity>
}