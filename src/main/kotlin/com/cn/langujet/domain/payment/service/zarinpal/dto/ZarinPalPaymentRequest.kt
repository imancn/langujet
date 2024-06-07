package com.cn.langujet.domain.payment.service.zarinpal.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ZarinPalPaymentRequest(
    @field:JsonProperty("merchant_id")
    val merchantId: String,
    val amount: Int,
    @field:JsonProperty("callback_url")
    val callbackUrl: String,
    val description: String,
    val metadata: ZarinPalRequestMetadata
)

data class ZarinPalRequestMetadata(val mobile: String, val email: String)