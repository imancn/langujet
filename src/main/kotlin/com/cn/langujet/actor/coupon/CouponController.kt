package com.cn.langujet.actor.coupon

import com.cn.langujet.actor.coupon.payload.request.CreateCouponRequest
import com.cn.langujet.actor.coupon.payload.response.ActiveCouponsResponse
import com.cn.langujet.actor.coupon.payload.response.CouponValidationResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.domain.coupon.Coupon
import com.cn.langujet.domain.coupon.CouponService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class CouponController(
    private val couponService: CouponService
) {
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/coupons/create")
    fun createCoupon(@RequestBody request: CreateCouponRequest): Coupon {
        return couponService.createCoupon(
            request.name,
            request.email,
            request.amount,
            request.tag,
            request.description
        )
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/coupons/validate") // todo: Should be changed to /student/coupons/validation
    fun couponValidation(@RequestParam couponCode: String): CouponValidationResponse {
        return couponService.couponValidation(couponCode)
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/coupons/active")
    fun getActiveCoupons(): List<ActiveCouponsResponse> {
        return couponService.getActiveCouponsByUserId(Auth.userId())
    }
}
