package com.cn.langujet.domain.payment.service

import com.cn.langujet.domain.payment.model.PaymentEntity
import com.cn.langujet.domain.payment.model.PaymentStatus
import com.cn.langujet.domain.payment.model.PaymentType
import com.cn.langujet.domain.payment.model.StripePaymentEntity
import com.cn.langujet.domain.payment.repository.PaymentRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentService(
    private val stripePaymentService: StripePaymentService,
    private val paymentRepository: PaymentRepository
) {
    fun createPayment(orderId: String, price: Double, paymentType: PaymentType): PaymentEntity {
        return when(paymentType) {
            PaymentType.STRIPE -> createStripePayment(orderId, price, paymentType)
            PaymentType.ZARIN_PAL -> createZarinPalPayment(orderId, price, paymentType)
        }
    }
    
    private fun createZarinPalPayment(orderId: String, price: Double, paymentType: PaymentType): PaymentEntity {
        TODO("Not yet implemented")
    }
    
    private fun createStripePayment(orderId: String, price: Double, paymentType: PaymentType): PaymentEntity {
        val stripeSession = stripePaymentService.createPaymentSession(price * 100.0)
        return paymentRepository.save(
            StripePaymentEntity(
                orderId = orderId,
                status = PaymentStatus.PENDING,
                paymentType = paymentType,
                link = stripeSession.url,
                createdDate = Date(System.currentTimeMillis()),
                lastModifiedDate = Date(System.currentTimeMillis()),
                sessionId = stripeSession.id
            )
        )
    }
    
}
