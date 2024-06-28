package com.cn.langujet.actor.order.payload

import com.cn.langujet.domain.order.model.OrderEntity
import com.cn.langujet.domain.order.model.OrderStatus
import com.cn.langujet.domain.payment.model.PaymentType
import java.util.*

data class StudentOrderPaymentResultResponse(
    val status: OrderStatus,
    val paymentType: PaymentType?,
    val totalPrice: Double,
    val finalPrice: Double,
    val date: Date,
) {
    constructor(orderEntity: OrderEntity) : this(
        orderEntity.status,
        orderEntity.paymentType,
        orderEntity.totalPrice,
        orderEntity.finalPrice,
        orderEntity.date
    )
}
