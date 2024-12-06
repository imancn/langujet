package com.cn.langujet.domain.payment.service

import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.payment.model.*
import com.cn.langujet.domain.payment.repository.PaymentRepository
import com.cn.langujet.domain.payment.service.zarinpal.ZarinPalPaymentService
import org.springframework.stereotype.Service

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
        val zarinPalPayment = zarinPalPaymentService.createPaymentSession(amountInIRR, orderId)
        return paymentRepository.save(
            ZarinPalPaymentEntity(
                orderId = orderId,
                status = PaymentStatus.PENDING,
                paymentType = paymentType,
                amount = amount,
                link = zarinPalPayment.url,
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
        /**
         * Remove it when Stripe be available
         */
        throw UnprocessableException("Not available in your country")
        val stripeSession = stripePaymentService.createPaymentSession(amount, orderId)
        return paymentRepository.save(
            StripePaymentEntity(
                orderId = orderId,
                status = PaymentStatus.PENDING,
                paymentType = paymentType,
                amount = amount,
                link = stripeSession.url,
                sessionId = stripeSession.id
            )
        )
    }
}
