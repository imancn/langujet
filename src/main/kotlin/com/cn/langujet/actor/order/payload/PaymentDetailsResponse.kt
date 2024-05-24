package com.cn.langujet.actor.order.payload

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse

data class PaymentDetailsResponse(
    val paymentMethods: List<PaymentMethodResponse>,
    val coupons: List<ActiveCouponsResponse>,
)

class PaymentMethodResponse(
    val key: String, // Todo: this argument type should be change to PaymentMethod from String
    val name: String,
    val icon: String
)
