package com.cn.langujet.domain.payment.service.zarinpal.dto

data class ZarinPalPaymentResponse(
    val data: ZarinPalPaymentData?,
    val errors: ZarinPalError?
) {
    constructor(map : Map<String, Any>) : this(
        data = if (map.containsKey("data") && map["data"] !is List<*>) {
            val dataMap = map["data"] as Map<*, *>
            ZarinPalPaymentData(
                dataMap["authority"] as String,
                dataMap["fee"] as Int
            )
        } else null,
        errors = if (map.containsKey("errors") && map["errors"] !is List<*>) {
            val errorMap = map["errors"] as Map<*, *>
            ZarinPalError(
                errorMap["code"] as Int,
                errorMap["message"] as String
            )
        } else null
    )
}

data class ZarinPalPaymentData(
    val authority: String,
    val fee: Int
)