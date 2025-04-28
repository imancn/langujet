package com.cn.langujet.actor.order

import com.cn.langujet.actor.order.payload.*
import com.cn.langujet.application.service.users.Auth
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.domain.coupon.CouponService
import com.cn.langujet.domain.order.model.OrderStatus
import com.cn.langujet.domain.order.service.OrderService
import com.cn.langujet.domain.payment.model.PaymentType
import com.cn.langujet.domain.region.ClientRegionService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/student/orders")
@Validated
class OrderStudentController(
    private val orderService: OrderService,
    private val couponService: CouponService,
    private val clientRegionService: ClientRegionService
) {
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    fun submitOrder(
        @Valid @RequestBody request: SubmitOrderRequest,
    ): SubmitOrderResponse {
        return orderService.submitOrder(request)
    }
    
    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    fun getOrders(
        @RequestParam orderStatus: OrderStatus?,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
    ): PageResponse<StudentOrderResponse> {
        return orderService.getStudentOrders(
            orderStatus, pageNumber, pageSize
        )
    }
    
    @GetMapping("/details")
    fun getOrderDetails(
        @RequestParam orderId: Long
    ): StudentOrderDetailsResponse {
        return orderService.getStudentOrderDetails(orderId)
    }
    
    @GetMapping("/payments/result")
    fun getOrderPaymentResult(
        @RequestParam orderId: Long
    ): StudentOrderPaymentResultResponse {
        return orderService.getOrderPaymentResult(orderId)
    }
    
    @GetMapping("/checkout")
    @PreAuthorize("hasRole('STUDENT')")
    fun getPaymentDetails(
        request: HttpServletRequest,
    ): PaymentDetailsResponse {
        return PaymentDetailsResponse(
            generatePaymentMethodList(getClientIp(request)),
            couponService.getCouponsByUserId(Auth.userId(), true)
        )
    }
    
    private fun getClientIp(request: HttpServletRequest): String? {
        try {
            val header = request.getHeader("X-Forwarded-For")
            return if (header == null || header.isEmpty()) {
                request.remoteAddr
            } else {
                header.split(",")[0]
            }
        } catch (ex: Exception) {
            return null
        }
    }
    
    private fun generatePaymentMethodList(clientIp: String?): List<PaymentMethodResponse> {
        val paymentMethodList = mutableListOf<PaymentMethodResponse>()
        paymentMethodList.add(
            PaymentMethodResponse(
                PaymentType.STRIPE.name,
                PaymentType.STRIPE.displayName,
                PaymentType.STRIPE.icon,
                1.0,
                Currency.EUR
            )
        )
        if (clientIp != null && clientRegionService.isFromIran(clientIp)) {
            paymentMethodList.add(
                PaymentMethodResponse(
                    PaymentType.ZARIN_PAL.name,
                    PaymentType.ZARIN_PAL.displayName,
                    PaymentType.ZARIN_PAL.icon,
                    60_000.0, // @Todo: it should change to live Bonbast.com ratio
                    Currency.IRT
                )
            )
        }
        return paymentMethodList
    }
}