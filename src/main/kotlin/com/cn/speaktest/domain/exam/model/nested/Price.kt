package com.cn.speaktest.domain.exam.model.nested

import com.cn.speaktest.actor.exam.payload.dto.PriceDto
import java.util.*

class Price(
    var value: Double, var currency: Currency
) {
    constructor(price: PriceDto) : this(
        price.value,
        price.currency
    )
}
