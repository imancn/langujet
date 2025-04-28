package com.cn.langujet.actor.coupon.payload.response

import com.cn.langujet.domain.coupon.CouponEntity

data class ActiveCouponsResponse(
    val code: String,
    val amount: Double,
    val percent : Int,
) {
    constructor(coupon: CouponEntity): this(
        code = coupon.code,
        amount = coupon.amount,
        percent = coupon.percentage,
    )
}
