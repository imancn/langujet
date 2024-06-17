package com.cn.langujet.actor.order

import com.cn.langujet.actor.order.payload.*
import com.cn.langujet.actor.order.payload.Currency
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.domain.coupon.CouponService
import com.cn.langujet.domain.order.UserDeviceType
import com.cn.langujet.domain.order.service.OrderService
import com.cn.langujet.domain.payment.model.PaymentType
import com.cn.langujet.domain.region.ClientRegionService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import java.util.*


@RestController
@RequestMapping("/api/v1")
@Validated
class OrderController(
    private val orderService: OrderService,
    private val couponService: CouponService,
    private val clientRegionService: ClientRegionService
) {
    @PostMapping("/student/orders")
    @PreAuthorize("hasRole('STUDENT')")
    fun submitOrder(
        @Valid @RequestBody request: SubmitOrderRequest,
    ): SubmitOrderResponse {
        return orderService.submitOrder(request)
    }
    
    @GetMapping("/student/orders/checkout")
    @PreAuthorize("hasRole('STUDENT')")
    fun getPaymentDetails(
        request: HttpServletRequest,
    ): PaymentDetailsResponse {
        return PaymentDetailsResponse(
            generatePaymentMethodList(getClientIp(request)),
            couponService.getCouponsByUserId(Auth.userId(), true)
        )
    }
    
    fun getClientIp(request: HttpServletRequest): String? {
        try {
            val header = request.getHeader("X-Forwarded-For")
            return if (header == null || header.isEmpty()) {
                request.remoteAddr
            } else {
                header.split(",")[0]
            }
        } catch (ex: Exception) {
            return null
        }
    }
    
    private fun generatePaymentMethodList(clientIp: String?): List<PaymentMethodResponse> {
        val paymentMethodList = mutableListOf<PaymentMethodResponse>()
        paymentMethodList.add(
            PaymentMethodResponse(
                PaymentType.STRIPE.name,
                PaymentType.STRIPE.displayName,
                PaymentType.STRIPE.icon,
                1.0,
                Currency.EUR
            )
        )
        if (clientIp != null && clientRegionService.isFromIran(clientIp)) {
            paymentMethodList.add(
                PaymentMethodResponse(
                    PaymentType.ZARIN_PAL.name,
                    PaymentType.ZARIN_PAL.displayName,
                    PaymentType.ZARIN_PAL.icon,
                    60_000.0, // @Todo: it should change to live Bonbast.com ratio
                    Currency.IRT
                )
            )
        }
        return paymentMethodList
    }
    
    @GetMapping("redirect/test/js")
    fun redirectTest(@RequestParam userDeviceType: UserDeviceType): String {
        val status = "successful"
        val id = UUID.randomUUID().toString()
        val amount = 20
        return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Redirect to App</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <script type=\"text/javascript\">\n" +
            "        // Deep link structure\n" +
            "        var status = \"failure\";\n" +
            "        var id = \"331a7089-2dfd-4a89-9fc9-8603398a6a22\";\n" +
            "        var amount = 200;\n" +
            "        var deepLinkUrl = `com.gorilsoft.langujet://launch/payment?status=${status}&id=${id}&amount=${amount}`;\n" +
            "\n" +
            "        // Redirect to the deep link\n" +
            "        window.location = deepLinkUrl;\n" +
            "    </script>\n" +
            "</body>\n" +
            "</html>"
    }
    
    @GetMapping("redirect/test/redirect-view")
    fun redirectTestv2(@RequestParam userDeviceType: UserDeviceType): RedirectView {
        val status = "successful"
        val id = UUID.randomUUID().toString()
        val amount = 20
        val deepLinkUrl =
            String.format("com.gorilsoft.langujet://launch/payment?status=%s&id=%s&amount=%d", status, id, amount)
        
        val redirectView = RedirectView()
        redirectView.url = deepLinkUrl
        return redirectView
    }
}