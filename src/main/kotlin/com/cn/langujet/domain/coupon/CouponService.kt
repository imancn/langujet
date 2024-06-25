package com.cn.langujet.domain.coupon

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse
import com.cn.langujet.actor.coupon.payload.response.CouponValidationResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.security.SecureRandom
import kotlin.jvm.optionals.getOrElse

@Service
class CouponService(
    private val couponRepository: CouponRepository, private val userRepository: UserRepository
) {
    
    private val random = SecureRandom()
    private val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    private val maxAttempts = 100
    
    fun generateCouponCode(): String {
        var code: String
        var attempts = 0
        do {
            if (attempts >= maxAttempts) {
                throw UnprocessableException("Failed to generate a unique coupon code after $maxAttempts attempts")
            }
            code = (1..5).map { chars[random.nextInt(chars.length)] }.joinToString("")
            attempts++
        } while (couponRepository.findByCode(code) != null)
        return code
    }
    
    fun createCoupon(name: String, email: String, amount: Double, tag: String?, description: String?): Coupon {
        val code = generateCouponCode()
        val user = userRepository.findByEmail(email).getOrElse {
            throw UnprocessableException("user with email $email not found")
        }
        return couponRepository.save(
            Coupon(
                name = name, code = code, userId = user.id ?: "", amount = amount, tag = tag, description = description
            )
        )
    }
    
    fun couponValidation(code: String): CouponValidationResponse {
        val coupon = couponRepository.findByCode(code.uppercase())
        return if (coupon != null && coupon.userId == Auth.userId() && coupon.active) {
            CouponValidationResponse(isValid = true, message = "Coupon is valid")
        } else {
            CouponValidationResponse(isValid = false, message = "Coupon is invalid")
        }
    }
    
    fun invalidateCoupon(coupon: Coupon) {
        couponRepository.save(coupon.also { it.active = false })
        /// todo: do roll back when order was canceled or failed ://
    }
    
    fun getCouponsByUserId(userId: String, active: Boolean?): List<ActiveCouponsResponse> {
        val coupons = when(active) {
            null -> couponRepository.findByUserId(userId)
            else -> couponRepository.findByUserIdAndActive(userId, active)
        }
        return coupons.map {
            ActiveCouponsResponse(
                name = it.name,
                code = it.code,
                amount = it.amount,
                createdDate = it.createdDate,
            )
        }
    }
    
    fun getActiveCouponByCode(couponCode: String?): Coupon? {
        if (!couponCode.isNullOrBlank()) {
            val coupon = couponRepository.findByCode(couponCode.uppercase())
            if (coupon == null || !coupon.active)
                throw UnprocessableException("Coupon code $couponCode is invalid")
            return coupon
        } else {
            return null
        }
    }
    
    fun getCouponById(id: String): Coupon {
        return couponRepository.findById(id).orElseThrow {
            throw UnprocessableException("Payment with Id $id not found")
        }
    }
}
