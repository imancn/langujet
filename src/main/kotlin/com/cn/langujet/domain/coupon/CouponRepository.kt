package com.cn.langujet.domain.coupon

import org.springframework.data.mongodb.repository.MongoRepository

interface CouponRepository : MongoRepository<CouponEntity, Long> {
    fun findByCode(code: String): CouponEntity?
    fun findByUserId(userId: Long): List<CouponEntity>
    fun findByUserIdAndActive(userId: Long, active: Boolean): List<CouponEntity>
    fun existsByUserIdAndCampaignId(userId: Long, campaignId: Long): Boolean
}
