package com.cn.langujet.domain.payment.service.zarinpal

import com.cn.langujet.application.arch.Auth
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.order.service.OrderService
import com.cn.langujet.domain.payment.model.PaymentStatus
import com.cn.langujet.domain.payment.model.ZarinPalPaymentEntity
import com.cn.langujet.domain.payment.repository.ZarinPalPaymentRepository
import com.cn.langujet.domain.payment.service.zarinpal.dto.ZarinPalVerifyRequest
import com.cn.langujet.domain.payment.service.zarinpal.dto.ZarinPalVerifyResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.servlet.view.RedirectView

@Service
class ZarinPalWebhookService(
    private val zarinPalClient: ZarinPalClient,
    override var repository: ZarinPalPaymentRepository,
    private val orderService: OrderService
) : HistoricalEntityService<ZarinPalPaymentRepository, ZarinPalPaymentEntity>() {
    @Value("\${payment.redirect.url}")
    private lateinit var paymentRedirectUrl: String
    
    @Value("\${zarin.pal.merchant.id}")
    private lateinit var merchantId: String
    
    fun handleWebhook(authority: String, status: String): RedirectView {
        Auth.setCustomUserId(Auth.EXTERNAL_SERVICE)
        val payment = repository.findByAuthority(authority).orElseThrow {
            UnprocessableException("Payment not found with authority = $authority")
        }
        if (!orderService.isAwaitingPayment(payment.orderId)) {
            return RedirectView("$paymentRedirectUrl?id=${payment.orderId}")
        }
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
                orderService.processOrder(payment.orderId)
            } else {
                payment.status = PaymentStatus.FAILED
                orderService.rejectOrder(payment.orderId)
            }
        } else { // status == NOK
            payment.status = PaymentStatus.FAILED
            orderService.rejectOrder(payment.orderId)
        }
        save(payment)
        return RedirectView("$paymentRedirectUrl?id=${payment.orderId}")
    }
}