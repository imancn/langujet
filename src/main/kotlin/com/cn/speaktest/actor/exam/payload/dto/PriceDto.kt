package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.Price
import java.util.*

data class PriceDto(
    val value: Double,
    val currency: Currency,
) {
    constructor(price: Price) : this(
        price.value,
        price.currency
    )
}