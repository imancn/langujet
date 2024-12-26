package com.cn.langujet.domain.coupon

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse
import com.cn.langujet.actor.coupon.payload.response.VerifyUserCoupon
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.user.services.UserService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.SecureRandom

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val userService: UserService
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
    
    fun createCoupon(name: String, email: String?, amount: Double, percentage: Int, tag: String?, description: String?): CouponEntity {
        if (percentage > 100 || percentage < 1) throw UnprocessableException("Percentage must be between 1% to 100%")
        val code = generateCouponCode()
        val user = email?.let { userService.getUserByEmail(it) }
        return couponRepository.save(
            CouponEntity(
                name = name, code = code, userId = user?.id, amount = amount, percentage = percentage, tag = tag, description = description
            )
        )
    }
    
    fun validateUserCoupon(code: String): VerifyUserCoupon {
        val coupon = couponRepository.findByCode(code.uppercase())
        return if (coupon != null && coupon.userId == Auth.userId() && coupon.active) {
            VerifyUserCoupon(isValid = true, message = "Coupon is valid")
        } else {
            VerifyUserCoupon(isValid = false, message = "Coupon is invalid")
        }
    }
    
    fun verifyUserCoupon(code: String): VerifyUserCoupon {
        val coupon = couponRepository.findByCode(code.uppercase())
        
        return try {
            verifyUserCoupon(coupon)
            VerifyUserCoupon(isValid = true, message = "Coupon is valid")
        } catch (e: UnprocessableException) {
            VerifyUserCoupon(isValid = false, message = "Coupon is invalid")
        }
    }
    
    fun verifyUserCoupon(coupon: CouponEntity?): CouponEntity {
        return if (coupon == null) {
            throw UnprocessableException("Coupon is invalid")
        } else if (!coupon.active) {
            throw UnprocessableException("Coupon is invalid")
        } else if (coupon.userId == null) {
            coupon.userId = Auth.userId()
            couponRepository.save(coupon)
        } else if (coupon.userId == Auth.userId()) {
            coupon
        } else  {
            throw UnprocessableException("Coupon is invalid")
        }
    }
    
    fun changeCouponActiveFlag(coupon: CouponEntity, active: Boolean) {
        couponRepository.save(coupon.also { it.active = active })
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
                percent = it.percentage,
                createdDate = it.createdAt,
            )
        }
    }
    
    fun getUserActiveCouponByCode(couponCode: String?): CouponEntity? {
        if (!couponCode.isNullOrBlank()) {
            val coupon = couponRepository.findByCode(couponCode.uppercase())
            if (coupon == null || !coupon.active)
                throw UnprocessableException("Coupon code $couponCode is invalid")
            verifyUserCoupon(coupon)
            return coupon
        } else {
            return null
        }
    }
    
    fun getCouponById(id: String): CouponEntity {
        return couponRepository.findById(id).orElseThrow {
            throw UnprocessableException("Payment with Id $id not found")
        }
    }
    
    fun calculateCouponFinalAmount(coupon: CouponEntity, purchasableAmount: BigDecimal): BigDecimal {
        val percentageAmount = purchasableAmount * coupon.percentage.toBigDecimal() / BigDecimal(100)
        val finalAmount = if (percentageAmount < coupon.amount.toBigDecimal()) {
            percentageAmount
        } else {
            coupon.amount.toBigDecimal()
        }
        return finalAmount.setScale(2, RoundingMode.HALF_UP)
    }
}
