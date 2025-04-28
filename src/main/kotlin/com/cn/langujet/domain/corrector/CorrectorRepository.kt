package com.cn.langujet.domain.corrector

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import java.util.*

interface CorrectorRepository : HistoricalMongoRepository<CorrectorEntity> {
    fun findByUser_Id(userId: Long): Optional<CorrectorEntity>
    fun existsByUser_Id(userId: Long): Boolean
}