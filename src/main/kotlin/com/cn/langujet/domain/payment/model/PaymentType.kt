package com.cn.langujet.domain.payment.model

enum class PaymentType(var displayName: String, val icon: String) {
    STRIPE("Stripe", "https://images.ctfassets.net/fzn2n1nzq965/HTTOloNPhisV9P4hlMPNA/cacf1bb88b9fc492dfad34378d844280/Stripe_icon_-_square.svg"),
    ZARIN_PAL("زرین پال", "https://bongo.ir/assets//images/zarinpal.svg"),
}
