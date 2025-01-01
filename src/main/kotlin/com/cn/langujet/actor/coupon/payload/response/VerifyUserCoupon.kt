package com.cn.langujet.actor.coupon.payload.response

class VerifyUserCoupon(val isValid: Boolean, val message: String, val coupon: ActiveCouponsResponse? = null) {}
