package com.cn.speaktest.exam.model

class Price(
    val value: Double, val currency: Currency
)

enum class Currency {
    IRT, USD, EUR, GBP
}
