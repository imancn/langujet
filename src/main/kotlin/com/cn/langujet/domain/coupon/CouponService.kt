package com.cn.langujet.domain.coupon

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse
import com.cn.langujet.actor.coupon.payload.response.VerifyUserCoupon
import com.cn.langujet.application.arch.Auth
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.campaign.CampaignService
import com.cn.langujet.domain.user.services.UserService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.SecureRandom

@Service
class CouponService(
    override var repository: CouponRepository,
    private val userService: UserService,
    private val campaignService: CampaignService
) : HistoricalEntityService<CouponRepository, CouponEntity>() {
    
    private val random = SecureRandom()
    private val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    private val maxAttempts = 100
    
    fun createCouponByUserEmail(campaignCode: String, email: String? = null): CouponEntity {
        val user = email?.let { userService.getUserByEmail(it) }
        return createCoupon(campaignCode, user?.id)
            ?: throw UnprocessableException("Failed to create a coupon for $campaignCode Campaign")
    }
    
    fun verifyUserCoupon(code: String): VerifyUserCoupon {
        return try {
            val coupon = verifyUserCoupon(repository.findByCode(code.uppercase()))
            VerifyUserCoupon(isValid = true, message = "Coupon is valid", ActiveCouponsResponse(coupon))
        } catch (e: UnprocessableException) {
            createCoupon(code, Auth.userId())?.let {
                VerifyUserCoupon(isValid = true, message = "Coupon is valid", ActiveCouponsResponse(it))
            } ?: VerifyUserCoupon(isValid = false, message = "Coupon is invalid")
        }
    }
    
    fun getCouponsByUserId(userId: Long, active: Boolean?): List<ActiveCouponsResponse> {
        val coupons = when (active) {
            null -> repository.findByUserId(userId)
            else -> repository.findByUserIdAndActive(userId, active)
        }
        return coupons.map {
            ActiveCouponsResponse(it)
        }
    }
    
    fun changeCouponActiveFlag(coupon: CouponEntity, active: Boolean) {
        save(coupon.also { it.active = active })
    }
    
    fun getUserActiveCouponByCode(couponCode: String?): CouponEntity? {
        if (!couponCode.isNullOrBlank()) {
            val coupon = repository.findByCode(couponCode.uppercase())
            verifyUserCoupon(coupon)
            return coupon
        } else {
            return null
        }
    }
    
    fun getCouponById(id: Long): CouponEntity {
        return repository.findById(id).orElseThrow {
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
        } while (repository.findByCode(code) != null)
        return code
    }
    
    private fun createCoupon(campaignCode: String, userId: Long? = null): CouponEntity? {
        val campaign = campaignService.getActiveCampaignByCode(campaignCode.uppercase()) ?: return null
        val campaignId = campaign.id ?: Entity.UNKNOWN_ID
        if (userId?.let { repository.existsByUserIdAndCampaignId(it, campaignId) } == true) {
            return null
        }
        val code = generateCouponCode()
        val coupon = save(
            CouponEntity(
                campaignId = campaignId,
                code = code,
                userId = userId,
                amount = campaign.amount,
                percentage = campaign.percentage,
            )
        )
        campaignService.useCampaign(campaignId)
        return coupon
    }
    
    private fun verifyUserCoupon(coupon: CouponEntity?): CouponEntity {
        return if (coupon == null) {
            throw UnprocessableException("Coupon is invalid")
        } else if (!coupon.active) {
            throw UnprocessableException("Coupon is invalid")
        } else if (coupon.userId == null) {
            coupon.userId = Auth.userId()
            save(coupon)
        } else if (coupon.userId == Auth.userId()) {
            coupon
        } else {
            throw UnprocessableException("Coupon is invalid")
        }
    }
}
