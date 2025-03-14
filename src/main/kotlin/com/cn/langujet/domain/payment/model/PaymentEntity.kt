package com.cn.langujet.domain.payment.model

import com.cn.langujet.application.shared.entity.HistoricalEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "payments")
@TypeAlias("payments")
sealed class PaymentEntity(
    @Id
    var id: String? = null,
    var orderId: String,
    var status: PaymentStatus,
    var paymentType: PaymentType,
    var amount: Double,
    var link: String
): HistoricalEntity()

@Document(collection = "payments")
@TypeAlias("stripe_payments")
class StripePaymentEntity(
    id: String? = null,
    orderId: String,
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
    id: String? = null,
    orderId: String,
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
