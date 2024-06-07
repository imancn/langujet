package com.cn.langujet.actor.payment.api

import com.cn.langujet.domain.payment.service.StripeWebhookService
import com.cn.langujet.domain.payment.service.zarinpal.ZarinPalWebhookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping("/api/v1")
class PaymentWebhookController(
    private val stripeWebhookService: StripeWebhookService,
    private val zarinPalWebhookService: ZarinPalWebhookService
) {
    @PostMapping("/stripe/checkout/session/webhook")
    fun handleStripeWebhook(
        @RequestBody payload: String,
        @RequestHeader("Stripe-Signature") signatureHeader: String
    ): ResponseEntity<String> {
        return stripeWebhookService.handleWebhook(payload, signatureHeader)
    }
    
    @GetMapping("/zarin-pal/payment/callback")
    fun handleZarinPalWebhook(
        @RequestParam("Authority") authority: String,
        @RequestParam("Status") status: String
    ) : RedirectView {
        return zarinPalWebhookService.handleWebhook(authority, status)
    }
}