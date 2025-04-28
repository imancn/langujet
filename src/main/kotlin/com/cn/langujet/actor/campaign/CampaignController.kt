package com.cn.langujet.actor.campaign

import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.campaign.CampaignEntity
import com.cn.langujet.domain.campaign.CampaignService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/campaigns")
class CampaignController(private val campaignService: CampaignService) :
    HistoricalEntityViewController<CampaignEntity>() {
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createCampaign(
        @RequestBody request: CreateCampaignRequest
    ): CampaignEntity {
        return campaignService.createCampaign(request)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/active")
    fun changeCampaignActiveFlag(
        @RequestParam campaignId: Long, @RequestParam active: Boolean
    ): CampaignEntity {
        return campaignService.changeCampaignActiveFlag(campaignId, active)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/metadata")
    fun changeCampaignMetadata(
        @RequestParam campaignId: Long, @RequestParam name: String? = null,
        @RequestParam tag: String? = null, @RequestParam description: String? = null
    ): CampaignEntity {
        return campaignService.changeCampaignMetadata(campaignId, name, tag, description)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usage-limit")
    fun changeUsageLimit(
        @RequestParam campaignId: Long, @RequestParam usageLimit: Int,
    ): CampaignEntity {
        return campaignService.changeUsageLimit(campaignId, usageLimit)
    }
}
