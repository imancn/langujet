package com.cn.langujet.domain.payment.service.zarinpal

import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.payment.service.zarinpal.dto.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ZarinPalPaymentService(
    private val zarinPalClient: ZarinPalClient
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    
    @Value("\${zarin.pal.merchant.id}")
    private lateinit var merchantId: String
    
    private val webhookCallbackUrl = "https://api.langujet.com/api/v1/zarin-pal/payment/callback"
    
    private val gateWayUrl: String = "https://sandbox.zarinpal.com/pg/StartPay/"
    
    fun createPaymentSession(amount: Int, orderId: String): ZarinPalPaymentDetails {
        try {
            val parameters = ZarinPalPaymentRequest(
                merchantId = merchantId,
                amount = amount,
                callbackUrl = webhookCallbackUrl,
                description = "OrderId: $orderId",
                metadata = ZarinPalRequestMetadata("N/A", Auth.userEmail())
            )
            val response = ZarinPalPaymentResponse(zarinPalClient.requestPayment(parameters))
            if (response.data?.authority != null) {
                val url = "$gateWayUrl${response.data.authority}"
                return ZarinPalPaymentDetails(url, response.data.authority, response.data.fee)
            } else {
                throw UnprocessableException("Zarin Pal payment failed.\nPlease contact to support\nError code:${response.errors?.code}")
            }
        } catch (ex: Exception) {
            logger.error("Exception occurred: ${ex.message}")
            throw UnprocessableException("Zarin Pal payment failed.\nPlease contact to support")
        }
    }
}