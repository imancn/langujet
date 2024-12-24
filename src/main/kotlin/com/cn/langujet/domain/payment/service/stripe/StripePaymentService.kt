package com.cn.langujet.domain.payment.service.stripe

import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class StripePaymentService(private val stripePaymentClient: StripePaymentClient) {
    @Value("\${stripe.api.secret.key}")
    private lateinit var apiSecretKey: String
    
    @Value("\${langujet.proxy.client.secret}")
    private lateinit var langujetProxyClientSecret: String
    
    @Value("\${payment.redirect.url}")
    private lateinit var paymentRedirectUrl: String
    
    @PostConstruct
    fun setup() {
        Stripe.apiKey = apiSecretKey
    }
    
    
    fun createPaymentSessionByProxy(amount: Double, orderId: String): StripeSessionInfo {
        return stripePaymentClient.createPaymentSession(amount, orderId, langujetProxyClientSecret)
    }
    
    /**
     * this method immediately unused because of sanctions
     */
    fun createPaymentSession(price: Double, orderId: String): Session {
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
                            .setUnitAmountDecimal(BigDecimal(price * 100.0))
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
            .setSuccessUrl("$paymentRedirectUrl?id=$orderId")
            .setCancelUrl("$paymentRedirectUrl?id=$orderId")
            .build()
        
        return Session.create(sessionParams)
    }
}





