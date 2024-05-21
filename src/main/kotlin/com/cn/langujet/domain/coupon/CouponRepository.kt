package com.cn.langujet.domain.coupon

import org.springframework.data.mongodb.repository.MongoRepository

interface CouponRepository : MongoRepository<Coupon, String> {
    fun findByCode(code: String): Coupon?
    fun findByUserIdAndActive(userId: String, active: Boolean = true): List<Coupon>
}
