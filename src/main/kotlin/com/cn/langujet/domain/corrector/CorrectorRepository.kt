package com.cn.langujet.domain.corrector

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface CorrectorRepository : MongoRepository<Corrector, String> {
    fun findByUser_Id(userId: String): Optional<Corrector>
    fun existsByUser_Id(userId: String): Boolean
}