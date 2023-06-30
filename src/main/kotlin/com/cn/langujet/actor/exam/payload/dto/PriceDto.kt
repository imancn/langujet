package com.cn.langujet.actor.exam.payload.dto

import com.cn.langujet.domain.exam.model.nested.Currency
import com.cn.langujet.domain.exam.model.nested.Price

data class PriceDto(
    val value: Double,
    val currency: Currency,
) {
    constructor(price: Price) : this(
        price.value,
        price.currency
    )
}