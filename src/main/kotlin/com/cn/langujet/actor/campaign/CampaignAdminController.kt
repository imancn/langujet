package com.cn.langujet.actor.campaign

import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.campaign.CampaignEntity
import com.cn.langujet.domain.campaign.CampaignService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/campaigns")
class CampaignAdminController(
    override var service: CampaignService,
) : HistoricalEntityViewController<CampaignService, CampaignEntity>() {
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createCampaign(
        @RequestBody request: CreateCampaignRequest
    ): CampaignEntity {
        return service.createCampaign(request)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/active")
    fun changeCampaignActiveFlag(
        @RequestParam campaignId: Long, @RequestParam active: Boolean
    ): CampaignEntity {
        return service.changeCampaignActiveFlag(campaignId, active)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/metadata")
    fun changeCampaignMetadata(
        @RequestParam campaignId: Long, @RequestParam name: String? = null,
        @RequestParam tag: String? = null, @RequestParam description: String? = null
    ): CampaignEntity {
        return service.changeCampaignMetadata(campaignId, name, tag, description)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/usage-limit")
    fun changeUsageLimit(
        @RequestParam campaignId: Long, @RequestParam usageLimit: Int,
    ): CampaignEntity {
        return service.changeUsageLimit(campaignId, usageLimit)
    }
}
