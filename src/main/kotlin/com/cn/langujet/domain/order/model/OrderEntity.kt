package com.cn.langujet.domain.order.model

import com.cn.langujet.domain.payment.model.PaymentType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "orders")
@TypeAlias("orders")
class OrderEntity(
    @Id
    var id: String? = null,
    var studentUserId: String,
    var paymentId: String?,
    var paymentType: PaymentType?,
    var couponId: String?,
    var status: OrderStatus,
    var totalPrice: Double,
    var finalPrice: Double,
    var date: Date
)