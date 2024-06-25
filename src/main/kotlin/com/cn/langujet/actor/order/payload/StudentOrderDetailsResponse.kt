package com.cn.langujet.actor.order.payload

import com.cn.langujet.domain.order.model.OrderEntity
import com.cn.langujet.domain.order.model.OrderStatus
import com.cn.langujet.domain.payment.model.PaymentType
import com.cn.langujet.domain.service.model.ServiceEntity
import java.util.*

data class StudentOrderDetailsResponse(
    val status: OrderStatus,
    val paymentType: PaymentType?,
    val totalPrice: Double,
    val finalPrice: Double,
    val date: Date,
    val couponCode: String?,
    val services: List<StudentOrderServicesResponse>
) {
    constructor(orderEntity: OrderEntity, couponCode: String?, services: List<ServiceEntity>) : this(
        orderEntity.status,
        orderEntity.paymentType,
        orderEntity.totalPrice,
        orderEntity.finalPrice,
        orderEntity.date,
        couponCode,
        services.map { StudentOrderServicesResponse.from(it) }
    )
}
