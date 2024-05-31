package com.cn.langujet.domain.payment.service

import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class StripePaymentService {
    @Value("\${stripe.api.secret.key}")
    private lateinit var apiSecretKey: String
    
    @PostConstruct
    fun setup() {
        Stripe.apiKey = apiSecretKey
    }
    
    fun createPaymentSession(priceInCent: Double): Session {
        val sessionParams = SessionCreateParams.builder()
            .addAllPaymentMethodType(
                listOf(
                    SessionCreateParams.PaymentMethodType.CARD,
                    SessionCreateParams.PaymentMethodType.PAYPAL,
                )
            )
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("eur")
                            .setUnitAmountDecimal(BigDecimal(priceInCent))
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Order price")
//                                    .setDescription("")
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("https://langujet.com/")
            .setCancelUrl("https://langujet.com/")
            .build()
        
        return Session.create(sessionParams)
    }
}





