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
    @PostMapping("/modify")
    fun modifyCampaign(
        @RequestBody request: CampaignEntity
    ): CampaignEntity {
        return campaignService.modifyCampaign(request)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{campaignId}")
    fun getCampaigns(
        @RequestParam active: Boolean?, @PathVariable campaignId: String?
    ): List<CampaignEntity> {
        return campaignService.getCampaigns(active, campaignId)
    }
}
