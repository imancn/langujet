package com.cn.speaktest.domain.exam.model

class Price(
    val value: Double, val currency: Currency
)

enum class Currency {
    IRT, USD, EUR, GBP
}
