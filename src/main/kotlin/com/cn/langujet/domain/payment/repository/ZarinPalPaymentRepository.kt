package com.cn.langujet.domain.payment.repository

import com.cn.langujet.domain.payment.model.PaymentStatus
import com.cn.langujet.domain.payment.model.PaymentType
import com.cn.langujet.domain.payment.model.ZarinPalPaymentEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ZarinPalPaymentRepository : MongoRepository<ZarinPalPaymentEntity, Long> {
    fun findByAuthority(authority: String): Optional<ZarinPalPaymentEntity>
    fun findByStatusAndPaymentType(pending: PaymentStatus, zarinPal: PaymentType = PaymentType.ZARIN_PAL): List<ZarinPalPaymentEntity>
}