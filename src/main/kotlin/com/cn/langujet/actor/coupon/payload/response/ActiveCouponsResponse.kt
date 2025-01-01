package com.cn.langujet.actor.coupon.payload.response

import com.cn.langujet.domain.coupon.CouponEntity
import java.util.*

data class ActiveCouponsResponse(
    val code: String,
    val amount: Double,
    val percent : Int,
    val createdDate: Date
) {
    constructor(coupon: CouponEntity): this(
        code = coupon.code,
        amount = coupon.amount,
        percent = coupon.percentage,
        createdDate = coupon.createdAt,
    )
}
