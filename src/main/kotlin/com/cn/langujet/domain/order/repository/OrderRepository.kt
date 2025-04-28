package com.cn.langujet.domain.order.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.order.model.OrderEntity
import com.cn.langujet.domain.order.model.OrderStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface OrderRepository : HistoricalMongoRepository<OrderEntity> {
    fun findAllByStudentUserIdAndStatusOrderByDateDesc(
        userId: Long,
        status: OrderStatus,
        pageRequest: PageRequest
    ): Page<OrderEntity>
    
    fun findAllByStudentUserIdOrderByDateDesc(userId: Long, pageRequest: PageRequest): Page<OrderEntity>
    fun countByStudentUserId(userId: Long): Long
    fun existsByStatusAndId(status: OrderStatus, id: Long): Boolean
}
