package com.cn.langujet.domain.order.service

import com.cn.langujet.actor.order.payload.SubmitOrderRequest
import com.cn.langujet.actor.order.payload.SubmitOrderResponse
import com.cn.langujet.domain.exam.service.ExamSessionService
import com.cn.langujet.domain.order.model.OrderEntity
import com.cn.langujet.domain.order.model.OrderStatus
import com.cn.langujet.domain.order.repository.OrderRepository
import com.cn.langujet.domain.payment.PaymentService
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import com.cn.langujet.domain.service.service.ServiceService
import com.cn.langujet.domain.student.service.StudentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val serviceService: ServiceService,
    private val paymentService: PaymentService,
    private val studentService: StudentService,
    private val examSessionService: ExamSessionService
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    fun submitOrder(auth: String, submitOrderRequest: SubmitOrderRequest): SubmitOrderResponse {
        val services = submitOrderRequest.serviceIds?.map { serviceService.getByIds(it) } ?: emptyList()

        val studentId = studentService.getStudentByAuthToken(auth).id ?: ""
        val totalPrice = services.sumOf { it.price - it.discount }

        if (totalPrice == 0.0) {
            val order = OrderEntity(
                id = null,
                studentId = studentId,
                status = OrderStatus.PAID,
                services = services,
                stripeSessionId = null,
                totalPrice = totalPrice,
                date = Date(System.currentTimeMillis())
            )
            orderRepository.save(order)
            processServices(studentId, services)
            return SubmitOrderResponse(null)
        } else {
            val session = paymentService.createPaymentSession(services)
            val order = OrderEntity(
                id = null,
                studentId = studentId,
                services = services,
                status = OrderStatus.PENDING,
                stripeSessionId = session.id,
                totalPrice = totalPrice,
                date = Date(System.currentTimeMillis())
            )
            orderRepository.save(order)
            return SubmitOrderResponse(session.url)
        }
    }

    fun processOrder(stripeSessionId: String) {
        val order = orderRepository.findByStripeSessionId(stripeSessionId)
        if (order != null) {
            processServices(order.studentId, order.services)
            orderRepository.save(
                order.also {
                    it.status = OrderStatus.PAID
                }
            )
        } else {
            logger.error("Order with Stripe Session Id $stripeSessionId not found")
        }
    }

    fun rejectOrder(stripeSessionId: String) {
        val order = orderRepository.findByStripeSessionId(stripeSessionId)
        if (order != null) {
            orderRepository.save(
                order.also {
                    it.status = OrderStatus.FAILED
                }
            )
        } else {
            logger.error("Order with Stripe Session Id $stripeSessionId not found")
        }
    }

    private fun processServices(
        studentId: String,
        services: List<ServiceEntity>
    ) {
        services.forEach {
            when(it.type) {
                ServiceType.EXAM -> {
                    val examService = it as ServiceEntity.ExamServiceEntity
                    examSessionService.enrollExamSession(studentId, examService.examVariantId)
                }
            }
        }
    }
}