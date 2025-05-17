package com.cn.langujet.actor.order

import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.order.model.OrderEntity
import com.cn.langujet.domain.order.service.OrderService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/orders")
@Validated
class OrderAdminController(
    override var service: OrderService
) : HistoricalEntityViewController<OrderService, OrderEntity>()