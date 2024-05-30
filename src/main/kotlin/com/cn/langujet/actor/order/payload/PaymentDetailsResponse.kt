package com.cn.langujet.actor.order.payload

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse
import com.cn.langujet.domain.payment.model.PaymentType

data class PaymentDetailsResponse(
    val paymentMethods: List<PaymentMethodResponse>,
    val coupons: List<ActiveCouponsResponse>,
    val payment: PaymentType = PaymentType.STRIPE
)

class PaymentMethodResponse(
    val key: String, // Todo: this argument type should be change to PaymentMethod from String
    val name: String,
    val icon: String
)
