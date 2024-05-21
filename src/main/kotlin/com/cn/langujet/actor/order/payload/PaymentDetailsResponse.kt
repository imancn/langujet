package com.cn.langujet.actor.order.payload

data class PaymentDetailsResponse(
    val paymentMethods: List<PaymentMethodResponse>,
)

class PaymentMethodResponse(
    val key: String, // Todo: this argument type should be change to PaymentMethod from String
    val name: String,
    val icon: String
)
