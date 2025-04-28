package com.cn.langujet.actor.coupon

import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse
import com.cn.langujet.actor.coupon.payload.response.VerifyUserCoupon
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.domain.coupon.CouponService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/student/coupons")
class CouponController(
    private val couponService: CouponService
) {
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/verify")
    fun verifyCoupon(@RequestParam couponCode: String): VerifyUserCoupon {
        return couponService.verifyUserCoupon(couponCode)
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    fun getCoupons(
        @RequestParam active: Boolean?
    ): List<ActiveCouponsResponse> {
        return couponService.getCouponsByUserId(Auth.userId(), active)
    }
}
