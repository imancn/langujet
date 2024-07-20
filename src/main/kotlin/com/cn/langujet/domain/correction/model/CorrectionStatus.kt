package com.cn.langujet.domain.correction.model

enum class CorrectionStatus(val order: Int) {
    PENDING(order = 1),
    PROCESSING(order = 2),
    PROCESSED(order = 3),
    REJECTED(order = 4),
    APPROVED(order = 5)
}