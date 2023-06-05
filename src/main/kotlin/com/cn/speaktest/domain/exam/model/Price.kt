package com.cn.speaktest.domain.exam.model

import com.cn.speaktest.actor.exam.payload.dto.PriceDto
import java.util.*

class Price(
    val value: Double, val currency: Currency
) {
    constructor(price: PriceDto) : this(
        price.value,
        price.currency
    )
}
