package com.cn.langujet.domain.payment.service.zarinpal.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ZarinPalVerifyRequest(
    var authority: String,
    var amount: Int,
    @field:JsonProperty("merchant_id")
    var merchantId: String
)