package com.cn.langujet.domain.order.service

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.order.model.OrderDetailEntity
import com.cn.langujet.domain.order.repository.OrderDetailRepository
import org.springframework.stereotype.Service

@Service
class OrderDetailService : HistoricalEntityService<OrderDetailRepository, OrderDetailEntity>()
