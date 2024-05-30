package com.cn.langujet.domain.payment.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("payments")
sealed class PaymentEntity(
    @Id
    var id: String? = null,
    var orderId: String,
    var status: PaymentStatus,
    var paymentType: PaymentType,
    var link: String,
    var createdDate: Date,
    var lastModifiedDate: Date
)

@Document("payments")
@TypeAlias("stripe_payments")
class StripePaymentEntity(
    id: String? = null,
    orderId: String,
    status: PaymentStatus,
    paymentType: PaymentType,
    link: String,
    createdDate: Date,
    lastModifiedDate: Date,
    var sessionId: String,
): PaymentEntity(
    id,
    orderId,
    status,
    paymentType,
    link,
    createdDate,
    lastModifiedDate
)
