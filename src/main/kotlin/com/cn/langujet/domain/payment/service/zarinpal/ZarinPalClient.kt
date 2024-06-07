package com.cn.langujet.domain.payment.service.zarinpal

import com.cn.langujet.domain.payment.service.zarinpal.dto.ZarinPalPaymentRequest
import com.cn.langujet.domain.payment.service.zarinpal.dto.ZarinPalVerifyRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "zarinPalClient", url = "https://api.zarinpal.com/pg/v4/payment/")
interface ZarinPalClient {
    @PostMapping(path = ["/request.json"])
    fun requestPayment(@RequestBody parameters: ZarinPalPaymentRequest): Map<String, Any>
    
    @PostMapping(path = ["/verify.json"], consumes = ["application/json"], produces = ["application/json"])
    fun verifyPayment(@RequestBody parameters: ZarinPalVerifyRequest): Map<String, Any>
}