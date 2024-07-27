package com.cn.langujet.domain.payment.model

enum class PaymentType(var displayName: String, val icon: String) {
    STRIPE("Stripe", "https://storage.langujet.com/static-files/icons/payments/stripe-logo.svg"),
    ZARIN_PAL("زرین پال", "https://storage.langujet.com/static-files/icons/payments/zarinpal-logo.svg"),
}
