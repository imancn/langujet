package com.cn.langujet.domain.exam.model.nested

import com.cn.langujet.actor.exam.payload.dto.PriceDto

class Price(
    var value: Double, var currency: Currency
) {
    constructor(price: PriceDto) : this(
        price.value,
        price.currency
    )
}
