package com.cn.langujet.actor.coupon.payload.request

data class CreateCouponRequest(
    val name: String,
    val email: String,
    val amount: Double,
    val tag: String?,
    val description: String?
)