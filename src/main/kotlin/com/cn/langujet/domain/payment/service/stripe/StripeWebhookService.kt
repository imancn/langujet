package com.cn.langujet.domain.payment.service.stripe

import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.order.service.OrderService
import com.cn.langujet.domain.payment.model.PaymentStatus
import com.cn.langujet.domain.payment.model.StripePaymentEntity
import com.cn.langujet.domain.payment.repository.StripePaymentRepository
import com.stripe.model.checkout.Session
import com.stripe.net.Webhook
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class StripeWebhookService(
    private val stripePaymentRepository: StripePaymentRepository,
    private val orderService: OrderService
) {
    @Value("\${stripe.webhook.secret}")
    private lateinit var webhookSecret: String
    
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    
    fun handleWebhook(
        payload: String,
        signatureHeader: String
    ): ResponseEntity<String> {
        try {
            val event = Webhook.constructEvent(payload, signatureHeader, webhookSecret)
            logger.info("${event.type} Event received")
            val session = event.data.`object` as Session
            val payment = stripePaymentRepository.findBySessionId(session.id).orElseThrow {
                UnprocessableException("Order with sessionId ${session.id} not found")
            }
            when (event.type) {
                "checkout.session.async_payment_failed" -> {
                    updatePaymentStatus(payment, PaymentStatus.FAILED)
                    orderService.rejectOrder(payment.orderId)
                }
                
                "checkout.session.async_payment_succeeded" -> {
                    updatePaymentStatus(payment, PaymentStatus.COMPLETED)
                    orderService.processOrder(payment.orderId)
                }
                
                "checkout.session.expired" -> {
                    updatePaymentStatus(payment, PaymentStatus.EXPIRED)
                    orderService.rejectOrder(payment.orderId)
                }
                
                "checkout.session.completed" -> { // @Todo: Revise this statuses action
                    when (session.paymentStatus) {
                        "paid", "no_payment_required" -> {
                            updatePaymentStatus(payment, PaymentStatus.COMPLETED)
                            orderService.processOrder(payment.orderId)
                        }
                        
                        "unpaid", "pending" -> {
                            updatePaymentStatus(payment, PaymentStatus.FAILED)
                            orderService.rejectOrder(payment.orderId)
                        }
                        
                        else -> {
                            logger.info("Unhandled session status: ${session.status} in ${event.type} event type")
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Unhandled session status: ${session.status} in ${event.type} event type")
                        }
                    }
                }
                
                else -> {
                    logger.info("Unhandled event type: ${event.type}")
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unhandled event type: ${event.type}")
                }
            }
            return ResponseEntity.ok("Event received")
        } catch (e: Exception) {
            logger.error("Webhook error: ${e.message}")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: ${e.message}")
        }
    }
    
    private fun updatePaymentStatus(payment: StripePaymentEntity, status: PaymentStatus) {
        payment.status = status
        payment.updateLog()
        stripePaymentRepository.save(payment)
    }
}