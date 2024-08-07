package com.cn.langujet.domain.payment.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "payments")
@TypeAlias("payments")
sealed class PaymentEntity(
    @Id
    var id: String? = null,
    var orderId: String,
    var status: PaymentStatus,
    var paymentType: PaymentType,
    var amount: Double,
    var link: String,
    var createdDate: Date,
    var lastModifiedDate: Date
)

@Document(collection = "payments")
@TypeAlias("stripe_payments")
class StripePaymentEntity(
    id: String? = null,
    orderId: String,
    status: PaymentStatus,
    paymentType: PaymentType,
    amount: Double,
    link: String,
    createdDate: Date,
    lastModifiedDate: Date,
    var sessionId: String,
): PaymentEntity(
    id,
    orderId,
    status,
    paymentType,
    amount,
    link,
    createdDate,
    lastModifiedDate
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
    createdDate: Date,
    lastModifiedDate: Date,
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
    link,
    createdDate,
    lastModifiedDate
)
