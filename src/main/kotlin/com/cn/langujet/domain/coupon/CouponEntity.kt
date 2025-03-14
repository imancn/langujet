package com.cn.langujet.domain.coupon

import com.cn.langujet.application.shared.entity.HistoricalEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "coupons")
@TypeAlias("coupons")
data class CouponEntity(
    @Id var id: String? = null,
    var campaignId: String,
    @Indexed(unique = true)
    var code: String,
    var userId: String? = null,
    var amount: Double,
    var percentage: Int,
    var active: Boolean = true,
): HistoricalEntity()
