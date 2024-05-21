package com.cn.langujet.domain.coupon

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse
import com.cn.langujet.actor.coupon.payload.response.ValidateCouponResponse
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
    
    fun validateCoupon(code: String): ValidateCouponResponse {
        val coupon = couponRepository.findByCode(code.uppercase())
        return if (coupon != null && coupon.userId == Auth.userId() && coupon.active) {
            ValidateCouponResponse(isValid = true, message = "Coupon is valid")
        } else {
            ValidateCouponResponse(isValid = false, message = "Coupon is invalid")
        }
    }
    
    fun getActiveCouponsByUserId(userId: String): List<ActiveCouponsResponse> {
        return couponRepository.findByUserIdAndActive(userId).map {
            ActiveCouponsResponse(
                name = it.name,
                code = it.code,
                amount = it.amount,
                createdDate = it.createdDate,
            )
        }
    }
}
