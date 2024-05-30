package com.cn.langujet.actor.payment.api

import com.cn.langujet.domain.payment.service.StripeWebhookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class StripeWebhookController(
    private val stripeWebhookService: StripeWebhookService
) {
    @PostMapping("/stripe/checkout/session/webhook")
    fun handleStripeWebhook(
        @RequestBody payload: String,
        @RequestHeader("Stripe-Signature") signatureHeader: String
    ): ResponseEntity<String> {
        return stripeWebhookService.handleWebhook(payload, signatureHeader)
    }
}