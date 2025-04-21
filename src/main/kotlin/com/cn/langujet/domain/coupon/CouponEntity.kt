package com.cn.langujet.domain.coupon

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "coupons")
@TypeAlias("coupons")
class CouponEntity(
    id: Long? = null,
    var campaignId: Long,
    @Indexed(name = "unique_code_index", unique = true)
    var code: String,
    var userId: Long? = null,
    var amount: Double,
    var percentage: Int,
    var active: Boolean = true,
) : HistoricalEntity(id = id)
