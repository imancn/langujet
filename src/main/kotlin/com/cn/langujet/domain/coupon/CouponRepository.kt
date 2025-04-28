package com.cn.langujet.domain.coupon

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository

interface CouponRepository : HistoricalMongoRepository<CouponEntity> {
    fun findByCode(code: String): CouponEntity?
    fun findByUserId(userId: Long): List<CouponEntity>
    fun findByUserIdAndActive(userId: Long, active: Boolean): List<CouponEntity>
    fun existsByUserIdAndCampaignId(userId: Long, campaignId: Long): Boolean
}
