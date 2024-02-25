package com.cn.langujet.actor.order.payload

import jakarta.validation.constraints.NotNull

data class SubmitOrderRequest(
    @field:NotNull val serviceIds: List<String>? = null,
    val promotionCode: String? = null
)
