package com.cn.langujet.domain.payment.service.zarinpal

import com.cn.langujet.domain.order.service.OrderService
import com.cn.langujet.domain.payment.model.PaymentStatus
import com.cn.langujet.domain.payment.repository.ZarinPalPaymentRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
class ZarinPalSessionTimeOutDetectorJob(
    private val zarinPalPaymentRepository: ZarinPalPaymentRepository,
    private val orderService: OrderService
) {
    @Scheduled(fixedDelay = 10 * 60 * 1000)
    fun failTimeoutSessions() {
        zarinPalPaymentRepository.findByStatusAndPaymentType(PaymentStatus.PENDING).forEach { payment ->
            val paymentCreatedDate = payment.createdAt?.time ?: 0
            if (paymentCreatedDate < Date().time - (15 * 60 * 1000)) {
                orderService.rejectOrder(payment.orderId)
                payment.status = PaymentStatus.EXPIRED
                zarinPalPaymentRepository.save(payment)
            }
        }
    }
}