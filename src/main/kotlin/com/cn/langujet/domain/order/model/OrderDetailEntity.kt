package com.cn.langujet.domain.order.model

import com.cn.langujet.application.arch.models.Log
import com.cn.langujet.domain.service.model.ServiceEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "order_details")
@TypeAlias("exam_order_details") /// We could move examSessionId to a child class and create another classes that extends OrderDetailEntity
class OrderDetailEntity(
    @Id
    var id: String? = null,
    @Indexed
    var orderId: String,
    var service: ServiceEntity,
    var examSessionId: String? = null,
) : Log()