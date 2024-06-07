package com.cn.langujet.domain.payment.service.zarinpal.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ZarinPalVerifyResponse(
    val data: ZarinPalVerifyData?,
    val errors: ZarinPalError?
) {
    constructor(map : Map<String, Any>) : this(
        data = if (map.containsKey("data") && map["data"] !is List<*>) {
            val dataMap = map["data"] as Map<*, *>
            ZarinPalVerifyData(
                dataMap["code"] as Int?,
                dataMap["message"] as String?,
                dataMap["cardHash"] as String?,
                dataMap["cardPan"] as String?,
                dataMap["refId"] as Int?,
                dataMap["feeType"] as String?,
                dataMap["fee"] as Int?,
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

data class ZarinPalVerifyData(
    val code: Int?,
    val message: String?,
    @JsonProperty("card_hash")
    val cardHash: String?,
    @JsonProperty("card_pan")
    val cardPan: String?,
    @JsonProperty("ref_id")
    val refId: Int?,
    @JsonProperty("fee_type")
    val feeType: String?,
    val fee: Int?
)