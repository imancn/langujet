package com.cn.langujet.actor.order

import com.cn.langujet.actor.order.payload.PaymentDetailsResponse
import com.cn.langujet.actor.order.payload.PaymentMethodResponse
import com.cn.langujet.actor.order.payload.SubmitOrderRequest
import com.cn.langujet.actor.order.payload.SubmitOrderResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.domain.coupon.CouponService
import com.cn.langujet.domain.order.service.OrderService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Validated
class OrderController(
    private val orderService: OrderService,
    private val couponService: CouponService
) {
    @PostMapping("/student/orders/submit")
    @PreAuthorize("hasRole('STUDENT')")
    fun submitOrder(
        @Valid @RequestBody request: SubmitOrderRequest,
    ): SubmitOrderResponse {
        return orderService.submitOrder(request)
    }
    
    @GetMapping("/student/orders/payment")
    @PreAuthorize("hasRole('STUDENT')")
    fun getPaymentDetails(): PaymentDetailsResponse { /// Todo: Modify this function after updating Order and Payment management
        return PaymentDetailsResponse(
            listOf(
                PaymentMethodResponse(
                    "STRIPE",
                    "Stripe",
                    "https://images.ctfassets.net/fzn2n1nzq965/HTTOloNPhisV9P4hlMPNA/cacf1bb88b9fc492dfad34378d844280/Stripe_icon_-_square.svg"
                )
            ),
            couponService.getActiveCouponsByUserId(Auth.userId())
        ) // Todo: Replace with PaymentMethods.values()
    }
}