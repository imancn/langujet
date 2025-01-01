package com.cn.langujet.actor.coupon

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse
import com.cn.langujet.actor.coupon.payload.response.VerifyUserCoupon
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.domain.coupon.CouponEntity
import com.cn.langujet.domain.coupon.CouponService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class CouponController(
    private val couponService: CouponService
) {
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/coupons")
    fun createCoupon(@RequestParam campaignId: String, @RequestParam userEmail: String?): CouponEntity {
        return couponService.createCouponByUserEmail(campaignId, userEmail)
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/coupons/verify")
    fun verifyCoupon(@RequestParam couponCode: String): VerifyUserCoupon {
        return couponService.verifyUserCoupon(couponCode)
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/coupons/")
    fun getCoupons(
        @RequestParam active: Boolean?
    ): List<ActiveCouponsResponse> {
        return couponService.getCouponsByUserId(Auth.userId(), active)
    }
}
