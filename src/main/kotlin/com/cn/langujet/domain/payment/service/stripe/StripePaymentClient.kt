package com.cn.langujet.domain.payment.service.stripe

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "stripePaymentClient", url = "https://api-proxy.langujet.com/")
interface StripePaymentClient {
    @PostMapping("api/v1/payments/stripe/create-session")
    fun createPaymentSession(
        @RequestParam price: Double, @RequestParam orderId: String, @RequestHeader("Authorization") langujetProxyClientSecret: String
    ): StripeSessionInfo
}

data class StripeSessionInfo(val url: String, val id: String)