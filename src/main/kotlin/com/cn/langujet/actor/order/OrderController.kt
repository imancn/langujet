package com.cn.langujet.actor.order

import com.cn.langujet.actor.order.payload.PaymentDetailsResponse
import com.cn.langujet.actor.order.payload.PaymentMethodResponse
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
        return PaymentDetailsResponse(
            listOf(
                PaymentMethodResponse(
                    "STRIPE",
                    "Stripe",
                    "https://images.ctfassets.net/fzn2n1nzq965/HTTOloNPhisV9P4hlMPNA/cacf1bb88b9fc492dfad34378d844280/Stripe_icon_-_square.svg"
                )
            )
        ) // Todo: Replace with PaymentMethods.values()
    }
}