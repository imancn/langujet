package com.cn.langujet.domain.payment.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.payment.model.StripePaymentEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StripePaymentRepository : HistoricalMongoRepository<StripePaymentEntity> {
    fun findBySessionId(id: String): Optional<StripePaymentEntity>
}