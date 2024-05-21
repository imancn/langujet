package com.cn.langujet.actor.order.payload

data class PaymentDetailsResponse(
    val paymentMethods: List<String>, // Todo: It should be replaced with List<PaymentMethods>
)
