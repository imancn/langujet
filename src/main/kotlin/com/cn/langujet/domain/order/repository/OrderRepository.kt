package com.cn.langujet.domain.order.repository

import com.cn.langujet.domain.order.model.OrderEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface OrderRepository: MongoRepository<OrderEntity, String> {
    fun findByStripeSessionId(stripeSessionId: String): OrderEntity?
}
