package com.cn.langujet.domain.coupon

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

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
    var createdDate: Date = Date(System.currentTimeMillis()),
)
