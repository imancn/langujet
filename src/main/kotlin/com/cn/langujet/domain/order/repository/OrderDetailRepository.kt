package com.cn.langujet.domain.order.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.order.model.OrderDetailEntity

interface OrderDetailRepository : HistoricalMongoRepository<OrderDetailEntity> {
    fun findByOrderId(orderId: Long): List<OrderDetailEntity>
}
