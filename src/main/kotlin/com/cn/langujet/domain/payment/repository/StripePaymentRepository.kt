package com.cn.langujet.domain.payment.repository

import com.cn.langujet.domain.payment.model.StripePaymentEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface StripePaymentRepository: MongoRepository<StripePaymentEntity, String> {
    fun findBySessionId(id: String): Optional<StripePaymentEntity>
}