package com.cn.langujet.actor.order.payload

import com.cn.langujet.domain.order.model.OrderEntity
import com.cn.langujet.domain.order.model.OrderStatus
import java.util.*

data class StudentOrderResponse(
    val orderId: String?,
    val status: OrderStatus,
    val paymentType: String?,
    val totalPrice: Double,
    val finalPrice: Double,
    val date: Date
) {
    constructor(orderEntity: OrderEntity) : this(
        orderEntity.id,
        orderEntity.status,
        orderEntity.paymentType?.displayName,
        orderEntity.totalPrice,
        orderEntity.finalPrice,
        orderEntity.date
    )
}
