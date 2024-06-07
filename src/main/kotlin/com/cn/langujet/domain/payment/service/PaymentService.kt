package com.cn.langujet.domain.payment.service

import com.cn.langujet.domain.payment.model.*
import com.cn.langujet.domain.payment.repository.PaymentRepository
import com.cn.langujet.domain.payment.service.zarinpal.ZarinPalPaymentService
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentService(
    private val stripePaymentService: StripePaymentService,
    private val paymentRepository: PaymentRepository,
    private val zarinPalPaymentService: ZarinPalPaymentService
) {
    fun createPayment(orderId: String, amount: Double, paymentType: PaymentType): PaymentEntity {
        return when(paymentType) {
            PaymentType.STRIPE -> createStripePayment(orderId, amount, paymentType)
            PaymentType.ZARIN_PAL -> createZarinPalPayment(orderId, amount, paymentType)
        }
    }
    
    private fun createZarinPalPayment(orderId: String, amount: Double, paymentType: PaymentType): PaymentEntity {
        val amountInIRR = (amount * 600_000).toInt() /// todo: it should be modified later and use exchange service
        val zarinPalPayment = zarinPalPaymentService.createPaymentSession(amountInIRR)
        return paymentRepository.save(
            ZarinPalPaymentEntity(
                orderId = orderId,
                status = PaymentStatus.PENDING,
                paymentType = paymentType,
                amount = amount,
                link = zarinPalPayment.url,
                createdDate = Date(System.currentTimeMillis()),
                lastModifiedDate = Date(System.currentTimeMillis()),
                authority = zarinPalPayment.authority,
                refId = null,
                amountInIRR = amountInIRR,
                fee = zarinPalPayment.fee,
                cardHash = null,
                cardPan = null
            )
        )
    }
    
    private fun createStripePayment(orderId: String, amount: Double, paymentType: PaymentType): PaymentEntity {
        val stripeSession = stripePaymentService.createPaymentSession(amount)
        return paymentRepository.save(
            StripePaymentEntity(
                orderId = orderId,
                status = PaymentStatus.PENDING,
                paymentType = paymentType,
                amount = amount,
                link = stripeSession.url,
                createdDate = Date(System.currentTimeMillis()),
                lastModifiedDate = Date(System.currentTimeMillis()),
                sessionId = stripeSession.id
            )
        )
    }
    
}
