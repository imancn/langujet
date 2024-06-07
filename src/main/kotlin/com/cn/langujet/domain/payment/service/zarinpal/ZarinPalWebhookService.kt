package com.cn.langujet.domain.payment.service.zarinpal

import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.order.service.OrderService
import com.cn.langujet.domain.payment.model.PaymentStatus
import com.cn.langujet.domain.payment.repository.ZarinPalPaymentRepository
import com.cn.langujet.domain.payment.service.zarinpal.dto.ZarinPalVerifyRequest
import com.cn.langujet.domain.payment.service.zarinpal.dto.ZarinPalVerifyResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Service
class ZarinPalWebhookService(
    private val zarinPalClient: ZarinPalClient,
    private val zarinPalPaymentRepository: ZarinPalPaymentRepository,
    private val orderService: OrderService
) {
    @Value("\${payment.redirect.url}")
    private lateinit var paymentRedirectUrl: String
    
    @Value("\${zarin.pal.merchant.id}")
    private lateinit var merchantId: String
    
    fun handleWebhook(authority: String, status: String): RedirectView {
        val payment = zarinPalPaymentRepository.findByAuthority(authority).orElseThrow {
            UnprocessableException("Payment not found with authority = $authority")
        }
        var redirectUrl: String? = null
        if (status == "OK") {
            val verifyResponse = ZarinPalVerifyResponse(
                zarinPalClient.verifyPayment(
                    ZarinPalVerifyRequest(
                        authority = authority,
                        amount = payment.amountInIRR,
                        merchantId = merchantId,
                    )
                )
            )
            if (verifyResponse.data?.code == 100) {
                payment.refId = verifyResponse.data.refId
                payment.cardHash = verifyResponse.data.cardHash
                payment.cardPan = verifyResponse.data.cardPan
                payment.status = PaymentStatus.COMPLETED
                redirectUrl = "$paymentRedirectUrl?status=success"
                orderService.processOrder(payment.orderId)
            } else {
                payment.status = PaymentStatus.FAILED
                redirectUrl = "$paymentRedirectUrl?status=failure"
                orderService.rejectOrder(payment.orderId)
            }
        } else { // status == NOK
            payment.status = PaymentStatus.FAILED
            redirectUrl = "$paymentRedirectUrl?status=failure"
            orderService.rejectOrder(payment.orderId)
        }
        payment.lastModifiedDate = Date(System.currentTimeMillis())
        zarinPalPaymentRepository.save(payment)
        return RedirectView(redirectUrl)
    }
}