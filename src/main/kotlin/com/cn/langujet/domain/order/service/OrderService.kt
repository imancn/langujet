package com.cn.langujet.domain.order.service

import com.cn.langujet.actor.order.payload.*
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.domain.coupon.CouponService
import com.cn.langujet.domain.exam.service.ExamSessionService
import com.cn.langujet.domain.order.model.OrderDetailEntity
import com.cn.langujet.domain.order.model.OrderEntity
import com.cn.langujet.domain.order.model.OrderStatus
import com.cn.langujet.domain.order.repository.OrderDetailRepository
import com.cn.langujet.domain.order.repository.OrderRepository
import com.cn.langujet.domain.payment.model.PaymentType
import com.cn.langujet.domain.payment.service.PaymentService
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import com.cn.langujet.domain.service.service.ServiceService
import com.rollbar.notifier.Rollbar
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderDetailRepository: OrderDetailRepository,
    private val serviceService: ServiceService,
    private val paymentService: PaymentService,
    private val examSessionService: ExamSessionService,
    private val couponService: CouponService,
    private val rollbar: Rollbar,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    
    fun submitOrder(submitOrderRequest: SubmitOrderRequest): SubmitOrderResponse {
        val services = submitOrderRequest.serviceIds.map { serviceService.getById(it) }
        
        val totalPrice = services.sumOf { it.price }
        val discountAmount = services.sumOf { it.discount }.toBigDecimal()
        val purchasableAmount = totalPrice.toBigDecimal() - discountAmount
        
        val coupon = couponService.getUserActiveCouponByCode(submitOrderRequest.couponCode)
        val couponFinalAmount = coupon?.let { couponService.calculateCouponFinalAmount(it, purchasableAmount) } ?: BigDecimal(0.0)
        
        var finalPrice = (purchasableAmount - couponFinalAmount).toDouble()
        if (finalPrice <= 0.0) { finalPrice = 0.0 }
        
        if (finalPrice == 0.0) {
            var order = OrderEntity(
                id = null,
                studentUserId = Auth.userId(),
                paymentId = null,
                paymentType = null,
                couponId = coupon?.id,
                status = OrderStatus.COMPLETED,
                totalPrice = totalPrice,
                finalPrice = finalPrice,
                date = Date(System.currentTimeMillis())
            )
            order = orderRepository.save(order)
            initiateOrderDetails(order, services)
            processOrder(order.id ?: "")
            coupon?.let { couponService.changeCouponActiveFlag(it, false) }
            return SubmitOrderResponse(null)
        } else {
            val paymentType = submitOrderRequest.paymentType ?: PaymentType.STRIPE
            val order = orderRepository.save(
                OrderEntity(
                    id = null,
                    studentUserId = Auth.userId(),
                    paymentId = null,
                    paymentType = paymentType,
                    couponId = coupon?.id,
                    status = OrderStatus.AWAITING_PAYMENT,
                    totalPrice = totalPrice,
                    finalPrice = finalPrice,
                    date = Date(System.currentTimeMillis())
                )
            )
            val payment = try {
                paymentService.createPayment(
                    order.id ?: "",
                    finalPrice,
                    paymentType
                )
            } catch (ex: Exception) {
                orderRepository.deleteById(order.id ?: "")
                rollbar.error(ex)
                ex.printStackTrace()
                throw UnprocessableException("Payment Failed")
            }
            order.paymentId = payment.id ?: ""
            orderRepository.save(order)
            initiateOrderDetails(order, services)
            coupon?.let { couponService.changeCouponActiveFlag(it, false) }
            return SubmitOrderResponse(payment.link)
        }
    }
    
    private fun initiateOrderDetails(order: OrderEntity, services: List<ServiceEntity>) {
        orderDetailRepository.saveAll(
            services.map { service ->
                OrderDetailEntity(
                    orderId = order.id ?: "",
                    service = service,
                )
            }
        )
    }
    
    fun processOrder(orderId: String) {
        val order = getOrderById(orderId)
        var orderDetails = orderDetailRepository.findByOrderId(orderId)
        orderDetails = orderDetails.map { orderDetail ->
            when (orderDetail.service.type) {
                ServiceType.EXAM -> {
                    val examService = orderDetail.service as ServiceEntity.ExamServiceEntity
                    val examSession = examSessionService.enrollExamSession(order.studentUserId, examService.id ?: "")
                    orderDetail.also { it.examSessionId = examSession.examSessionId }
                }
            }
        }
        orderDetailRepository.saveAll(orderDetails)
        orderRepository.save(
            order.also {
                it.status = OrderStatus.COMPLETED
            }
        )
    }
    
    fun rejectOrder(orderId: String) {
        val order = getOrderById(orderId)
        orderRepository.save(
            order.also {
                it.status = OrderStatus.FAILED
            }
        )
        order.couponId?.let {
            couponService.changeCouponActiveFlag(couponService.getCouponById(it), true)
        }
    }
    
    private fun getOrderById(orderId: String): OrderEntity {
        return orderRepository.findById(orderId).getOrElse {
            logger.error("Order with Id $orderId not found")
            throw UnprocessableException("Order with Id $orderId not found")
        }
    }
    
    fun getStudentOrders(orderStatus: OrderStatus?, pageNumber: Int, pageSize: Int): PageResponse<StudentOrderResponse> {
        val orders = if (orderStatus != null) {
            orderRepository.findAllByStudentUserIdAndStatusOrderByDateDesc(Auth.userId(), orderStatus, PageRequest.of(pageNumber, pageSize))
        } else {
            orderRepository.findAllByStudentUserIdOrderByDateDesc(Auth.userId(), PageRequest.of(pageNumber, pageSize))
        }
        val totalOrders = orderRepository.countByStudentUserId(Auth.userId())
        val orderResponse = orders.map { StudentOrderResponse(it) }
        return PageResponse(orderResponse.content, pageNumber, pageSize, totalOrders)
    }
    
    fun getStudentOrderDetails(orderId: String): StudentOrderDetailsResponse {
        val order = getOrderById(orderId)
        if (order.studentUserId != Auth.userId()) {
            throw UnprocessableException("You don't access to this order")
        }
        val orderDetails = orderDetailRepository.findByOrderId(orderId)
        val coupon = order.couponId?.let { couponService.getCouponById(it) }
        return StudentOrderDetailsResponse(
            order, coupon?.code, orderDetails.map { it.service }
        )
    }
    
    fun getOrderPaymentResult(orderId: String): StudentOrderPaymentResultResponse {
        val order = getOrderById(orderId)
        if (order.studentUserId != Auth.userId()) {
            throw UnprocessableException("You don't access to this order")
        }
        return StudentOrderPaymentResultResponse(order)
    }
    
    fun isAwaitingPayment(orderId: String): Boolean {
        return orderRepository.existsByStatusAndId(OrderStatus.AWAITING_PAYMENT, orderId)
    }
}