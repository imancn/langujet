package com.cn.langujet.domain.payment.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "payments")
@TypeAlias("payments")
sealed class PaymentEntity(
    id: Long? = null,
    var orderId: Long,
    var status: PaymentStatus,
    var paymentType: PaymentType,
    var amount: Double,
    var link: String
) : HistoricalEntity(id = id)

@Document(collection = "payments")
@TypeAlias("stripe_payments")
class StripePaymentEntity(
    id: Long? = null,
    orderId: Long,
    status: PaymentStatus,
    paymentType: PaymentType,
    amount: Double,
    link: String,
    var sessionId: String,
): PaymentEntity(
    id,
    orderId,
    status,
    paymentType,
    amount,
    link
)

@Document(collection = "payments")
@TypeAlias("zarin_pal_payments")
class ZarinPalPaymentEntity(
    id: Long? = null,
    orderId: Long,
    status: PaymentStatus,
    paymentType: PaymentType,
    amount: Double,
    link: String,
    var authority: String,
    var refId: Int?,
    var amountInIRR: Int,
    var fee: Int,
    var cardHash: String?,
    var cardPan: String?
): PaymentEntity(
    id,
    orderId,
    status,
    paymentType,
    amount,
    link
)
