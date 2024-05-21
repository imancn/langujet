package com.cn.langujet.actor.coupon.payload.response

import java.util.*

data class ActiveCouponsResponse(
    val name: String,
    val code: String,
    val amount: Double,
    val createdDate: Date
)
