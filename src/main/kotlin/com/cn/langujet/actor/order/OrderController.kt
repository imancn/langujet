package com.cn.langujet.actor.order

import com.cn.langujet.actor.order.payload.PaymentDetailsResponse
import com.cn.langujet.actor.order.payload.SubmitOrderRequest
import com.cn.langujet.actor.order.payload.SubmitOrderResponse
import com.cn.langujet.domain.order.service.OrderService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Validated
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping("/student/orders/submit")
    @PreAuthorize("hasRole('STUDENT')")
    fun submitOrder(
        @Valid @RequestBody request: SubmitOrderRequest,
    ): SubmitOrderResponse {
        return orderService.submitOrder(request)
    }
    
    @GetMapping("/student/orders/payment/")
    @PreAuthorize("hasRole('STUDENT')")
    fun getPaymentDetails(): PaymentDetailsResponse {
        return PaymentDetailsResponse(listOf("STRIPE")) // Todo: Replace with PaymentMethods.values()
    }
}