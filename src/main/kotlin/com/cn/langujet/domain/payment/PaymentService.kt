package com.cn.langujet.domain.payment

import com.cn.langujet.domain.service.model.ServiceEntity
import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PaymentService {

    @Value("\${stripe.api.secret.key}")
    private lateinit var apiSecretKey: String

    @PostConstruct
    fun setup() {
        Stripe.apiKey = apiSecretKey
    }

    fun createPaymentSession(services: List<ServiceEntity>): Session {
        val sessionParams = SessionCreateParams.builder()
            .addAllPaymentMethodType(
                listOf(
                    SessionCreateParams.PaymentMethodType.CARD,
                    SessionCreateParams.PaymentMethodType.PAYPAL
                )
            )
            .addAllLineItem(lineItems(services))
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("https://langujet.com/")
            .setCancelUrl("https://langujet.com/")
            .build()

        return Session.create(sessionParams)
    }

    private fun lineItems(services: List<ServiceEntity>): List<SessionCreateParams.LineItem> {
        return services.map {
            val priceInCent = (it.price - it.discount) * 100
            SessionCreateParams.LineItem.builder()
                .setQuantity(1)
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmountDecimal(BigDecimal(priceInCent))
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(it.name)
                                .setDescription(it.id)
                                .build()
                        )
                        .build()
                )
                .build()
        }
    }
}





