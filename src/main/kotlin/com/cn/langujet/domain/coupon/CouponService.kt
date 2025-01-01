package com.cn.langujet.domain.coupon

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse
import com.cn.langujet.actor.coupon.payload.response.VerifyUserCoupon
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.campaign.CampaignService
import com.cn.langujet.domain.user.services.UserService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.SecureRandom

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val userService: UserService,
    private val campaignService: CampaignService
) {
    
    private val random = SecureRandom()
    private val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    private val maxAttempts = 100
    
    fun createCouponByUserEmail(campaignCode: String, email: String? = null): CouponEntity {
        val user = email?.let { userService.getUserByEmail(it) }
        return createCoupon(campaignCode, user?.id)
            ?: throw UnprocessableException("Failed to create a coupon for $campaignCode")
    }
    
    fun verifyUserCoupon(code: String): VerifyUserCoupon {
        return try {
            val coupon = verifyUserCoupon(couponRepository.findByCode(code.uppercase()))
            VerifyUserCoupon(isValid = true, message = "Coupon is valid", ActiveCouponsResponse(coupon))
        } catch (e: UnprocessableException) {
            createCoupon(code, Auth.userId())?.let {
                VerifyUserCoupon(isValid = true, message = "Coupon is valid", ActiveCouponsResponse(it))
            } ?: VerifyUserCoupon(isValid = false, message = "Coupon is invalid")
        }
    }
    
    fun getCouponsByUserId(userId: String, active: Boolean?): List<ActiveCouponsResponse> {
        val coupons = when (active) {
            null -> couponRepository.findByUserId(userId)
            else -> couponRepository.findByUserIdAndActive(userId, active)
        }
        return coupons.map {
            ActiveCouponsResponse(it)
        }
    }
    
    fun changeCouponActiveFlag(coupon: CouponEntity, active: Boolean) {
        couponRepository.save(coupon.also { it.active = active })
    }
    
    fun getUserActiveCouponByCode(couponCode: String?): CouponEntity? {
        if (!couponCode.isNullOrBlank()) {
            val coupon = couponRepository.findByCode(couponCode.uppercase())
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
    
    private fun generateCouponCode(): String {
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
    
    private fun createCoupon(campaignCode: String, userId: String? = null): CouponEntity? {
        val campaign = campaignService.getActiveCampaignByCode(campaignCode.uppercase()) ?: return null
        if (userId?.let { couponRepository.existsByUserIdAndCampaignId(campaign.id ?: "", it) } == true) {
            return null
        }
        return if (campaignService.campaignExceededLimit(campaign.id ?: "")) { null } else {
            val code = generateCouponCode()
            couponRepository.save(
                CouponEntity(
                    campaignId = campaign.id ?: "",
                    code = code,
                    userId = userId,
                    amount = campaign.amount,
                    percentage = campaign.percentage,
                )
            )
        }
    }
    
    private fun verifyUserCoupon(coupon: CouponEntity?): CouponEntity {
        return if (coupon == null) {
            throw UnprocessableException("Coupon is invalid")
        } else if (!coupon.active) {
            throw UnprocessableException("Coupon is invalid")
        } else if (coupon.userId == null) {
            coupon.userId = Auth.userId()
            couponRepository.save(coupon)
        } else if (coupon.userId == Auth.userId()) {
            coupon
        } else {
            throw UnprocessableException("Coupon is invalid")
        }
    }
}
