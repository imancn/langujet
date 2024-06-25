package com.cn.langujet.domain.order.repository

import com.cn.langujet.domain.order.model.OrderEntity
import com.cn.langujet.domain.order.model.OrderStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface OrderRepository: MongoRepository<OrderEntity, String> {
    fun findAllByStudentUserIdAndStatusOrderByDateDesc(userId: String, status: OrderStatus, pageRequest: PageRequest): Page<OrderEntity>
    fun findAllByStudentUserIdOrderByDateDesc(userId: String, pageRequest: PageRequest): Page<OrderEntity>
    fun countByStudentUserId(userId: String): Long
}
