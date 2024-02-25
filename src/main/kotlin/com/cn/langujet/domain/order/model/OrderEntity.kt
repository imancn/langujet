package com.cn.langujet.domain.order.model

import com.cn.langujet.domain.service.model.ServiceEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("orders")
class OrderEntity(
    @Id
    var id: String? = null,
    val studentId: String,
    var status: OrderStatus,
    var services: List<ServiceEntity>,
    var stripeSessionId: String?,
    var totalPrice: Double,
    var date: Date
)