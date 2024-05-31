package com.cn.langujet.actor.order.payload

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse

data class PaymentDetailsResponse(
    val paymentMethods: List<PaymentMethodResponse>,
    val coupons: List<ActiveCouponsResponse>,
)

class PaymentMethodResponse(
    val key: String,
    val name: String,
    val icon: String,
    val exchangeRatio: Double,
    val currency: Currency,
)
