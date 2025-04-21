package com.cn.langujet.domain.order.repository

import com.cn.langujet.domain.order.model.OrderDetailEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface OrderDetailRepository : MongoRepository<OrderDetailEntity, Long> {
    fun findByOrderId(orderId: Long): List<OrderDetailEntity>
}
