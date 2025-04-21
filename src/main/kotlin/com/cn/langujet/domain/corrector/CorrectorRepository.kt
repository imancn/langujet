package com.cn.langujet.domain.corrector

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface CorrectorRepository : MongoRepository<CorrectorEntity, Long> {
    fun findByUser_Id(userId: Long): Optional<CorrectorEntity>
    fun existsByUser_Id(userId: Long): Boolean
}