package com.cn.langujet.actor.coupon

import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.coupon.CouponEntity
import com.cn.langujet.domain.coupon.CouponRepository
import com.cn.langujet.domain.coupon.CouponService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/coupons")
class CouponAdminController(
    private val couponService: CouponService
) : HistoricalEntityViewController<CouponRepository, CouponService, CouponEntity>() {
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createCoupon(@RequestParam campaignCode: String, @RequestParam userEmail: String?): CouponEntity {
        return couponService.createCouponByUserEmail(campaignCode, userEmail)
    }
}
