package com.cn.langujet.actor.campaign

import com.cn.langujet.domain.campaign.CampaignEntity
import com.cn.langujet.domain.campaign.CampaignService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/campaigns")
class CampaignController(private val campaignService: CampaignService) {
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createCampaign(
        @RequestBody request: CreateCampaignRequest
    ): CampaignEntity {
        return campaignService.createCampaign(request)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/active")
    fun changeCampaignActiveFlag(
        @RequestParam campaignId: Long, @RequestParam active: Boolean
    ): CampaignEntity {
        return campaignService.changeCampaignActiveFlag(campaignId, active)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/metadata")
    fun changeCampaignMetadata(
        @RequestParam campaignId: Long, @RequestParam name: String? = null,
        @RequestParam tag: String? = null, @RequestParam description: String? = null
    ): CampaignEntity {
        return campaignService.changeCampaignMetadata(campaignId, name, tag, description)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/usage-limit")
    fun changeUsageLimit(
        @RequestParam campaignId: Long, @RequestParam usageLimit: Int,
    ): CampaignEntity {
        return campaignService.changeUsageLimit(campaignId, usageLimit)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{campaignId}")
    fun getCampaigns(
        @RequestParam active: Boolean?, @PathVariable campaignId: Long?
    ): List<CampaignEntity> {
        return campaignService.getCampaigns(active, campaignId)
    }
}
