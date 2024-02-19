package com.cn.langujet.actor

import com.cn.langujet.domain.order.service.OrderService
import com.stripe.model.checkout.Session
import com.stripe.net.Webhook
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/stripe")
class StripeWebhookController(
    private val orderService: OrderService
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    @Value("\${stripe.webhook.secret}")
    private lateinit var webhookSecret: String

    @PostMapping("/checkout/session/webhook")
    fun handleStripeWebhook(@RequestBody payload: String, @RequestHeader("Stripe-Signature") signatureHeader: String): ResponseEntity<String> {
        try {
            val event = Webhook.constructEvent(payload, signatureHeader, webhookSecret)
            logger.info("${event.type} Event received")
            val session = event.data.`object` as Session
            when (event.type) {
                "checkout.session.async_payment_failed" -> {
                    orderService.rejectOrder(session.id)
                }
                "checkout.session.async_payment_succeeded" -> {
                    orderService.processOrder(session.id)
                }
                "checkout.session.expired" -> {
                    orderService.rejectOrder(session.id)
                }
                "checkout.session.completed" -> {
                    when(session.paymentStatus) {
                        "paid", "no_payment_required" -> {
                            orderService.processOrder(session.id)
                        }
                        "unpaid", "pending" -> {
                            orderService.rejectOrder(session.id)
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
}
