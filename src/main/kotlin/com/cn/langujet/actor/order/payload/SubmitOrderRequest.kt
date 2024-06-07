package com.cn.langujet.actor.order.payload

import com.cn.langujet.domain.payment.model.PaymentType

data class SubmitOrderRequest(
    val serviceIds: List<String>,
    val paymentType: PaymentType?,
    val couponCode: String?
)
