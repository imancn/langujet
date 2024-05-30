package com.cn.langujet.domain.order.repository

import com.cn.langujet.domain.order.model.OrderEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface OrderRepository: MongoRepository<OrderEntity, String>
