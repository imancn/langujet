package com.cn.langujet.domain.coupon

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "coupons")
data class Coupon(
    @Id val id: String? = null,
    val name: String,
    val code: String,
    val userId: String,
    val amount: Double,
    val active: Boolean = true,
    val tag: String? = null,
    val description: String? = null,
    val createdDate: Date = Date(System.currentTimeMillis()),
)
