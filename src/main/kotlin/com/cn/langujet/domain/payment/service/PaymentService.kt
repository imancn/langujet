package com.cn.langujet.domain.payment.service

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.payment.model.*
import com.cn.langujet.domain.payment.repository.PaymentRepository
import com.cn.langujet.domain.payment.service.stripe.StripePaymentService
import com.cn.langujet.domain.payment.service.zarinpal.ZarinPalPaymentService
import org.springframework.stereotype.Service

@Service
class PaymentService(
    override var repository: PaymentRepository,
    private val zarinPalPaymentService: ZarinPalPaymentService,
    private val stripePaymentService: StripePaymentService
) : HistoricalEntityService<PaymentRepository, PaymentEntity>() {
    fun createPayment(orderId: Long, amount: Double, paymentType: PaymentType): PaymentEntity {
        return when(paymentType) {
            PaymentType.STRIPE -> createStripePayment(orderId, amount, paymentType)
            PaymentType.ZARIN_PAL -> createZarinPalPayment(orderId, amount, paymentType)
        }
    }
    
    private fun createZarinPalPayment(orderId: Long, amount: Double, paymentType: PaymentType): PaymentEntity {
        val amountInIRR = (amount * 600_000).toInt() /// todo: it should be modified later and use exchange service
        val zarinPalPayment = zarinPalPaymentService.createPaymentSession(amountInIRR, orderId)
        return save(
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
    
    private fun createStripePayment(orderId: Long, amount: Double, paymentType: PaymentType): PaymentEntity {
        val stripeSession = stripePaymentService.createPaymentSessionByProxy(amount, orderId)
        return save(
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
