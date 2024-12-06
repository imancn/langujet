package com.cn.langujet.domain.coupon

import com.cn.langujet.application.shared.HistoricalEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "coupons")
@TypeAlias("coupons")
data class CouponEntity(
    @Id var id: String? = null,
    var name: String,
    var code: String,
    var userId: String,
    var amount: Double,
    var active: Boolean = true,
    var tag: String? = null,
    var description: String? = null,
): HistoricalEntity()
