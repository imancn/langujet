package com.cn.langujet.domain.payment.repository

import com.cn.langujet.domain.payment.model.ZarinPalPaymentEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ZarinPalPaymentRepository: MongoRepository<ZarinPalPaymentEntity, String> {
    fun findByAuthority(authority: String): Optional<ZarinPalPaymentEntity>
}