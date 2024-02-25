package com.cn.langujet.actor.order

import com.cn.langujet.actor.order.payload.SubmitOrderRequest
import com.cn.langujet.actor.order.payload.SubmitOrderResponse
import com.cn.langujet.domain.order.service.OrderService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Validated
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping("/student/orders/submit")
    fun submitOrder(
        @Valid @RequestBody request: SubmitOrderRequest,
        @RequestHeader("Authorization") @NotBlank auth: String?
    ): SubmitOrderResponse {
        return orderService.submitOrder(auth!!, request)
    }
}