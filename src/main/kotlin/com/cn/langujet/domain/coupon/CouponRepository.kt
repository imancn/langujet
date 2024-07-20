package com.cn.langujet.domain.coupon

import org.springframework.data.mongodb.repository.MongoRepository

interface CouponRepository : MongoRepository<CouponEntity, String> {
    fun findByCode(code: String): CouponEntity?
    fun findByUserId(userId: String): List<CouponEntity>
    fun findByUserIdAndActive(userId: String, active: Boolean): List<CouponEntity>
}
