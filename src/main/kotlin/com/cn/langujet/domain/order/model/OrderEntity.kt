package com.cn.langujet.domain.order.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.domain.payment.model.PaymentType
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "orders")
@TypeAlias("orders")
class OrderEntity(
    id: Long? = null,
    var studentUserId: Long,
    var paymentId: Long?,
    var paymentType: PaymentType?,
    var couponId: Long?,
    var status: OrderStatus,
    var totalPrice: Double,
    var finalPrice: Double,
    var date: Date
) : HistoricalEntity(id = id)