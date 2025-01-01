package com.cn.langujet.domain.campaign

import com.cn.langujet.actor.campaign.CreateCampaignRequest
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.user.services.UserService
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.toList

@Service
class CampaignService(
    private val campaignRepository: CampaignRepository, private val userService: UserService
) {
    fun createCampaign(request: CreateCampaignRequest): CampaignEntity {
        if (request.percentage > 100 || request.percentage < 1) throw UnprocessableException("Percentage must be between 1% to 100%")
        return campaignRepository.save(
            CampaignEntity(
                name = request.name,
                code = request.code.uppercase(),
                amount = request.amount,
                percentage = request.percentage,
                limit = request.limit,
                consumedTimes = request.consumedTimes,
                tag = request.tag,
                description = request.description
            )
        )
    }
    
    fun modifyCampaign(campaignEntity: CampaignEntity): CampaignEntity {
        if (campaignRepository.existsById(campaignEntity.id ?: "")) {
            return campaignRepository.save(campaignEntity)
        } else {
            throw UnprocessableException("Campaign with id ${campaignEntity.id} does not exist")
        }
    }
    
    fun getCampaigns(active: Boolean?, campaignId: String?): List<CampaignEntity> {
        campaignId?.let { return campaignRepository.findById(campaignId).toList() }
        return if (active == null) {
            campaignRepository.findAll()
        } else {
            campaignRepository.findAllByActive(active)
        }
    }
    
    fun getActiveCampaignByCode(code: String): CampaignEntity? {
        return campaignRepository.findByCodeAndActive(code, true)
    }
    
    fun consume(campaignId: String): Boolean {
        val campaign = campaignRepository.findById(campaignId).getOrElse { return false }
        if (!campaign.active) return false
        if (campaign.consumedTimes + 1 >= campaign.limit) {
            campaign.active = false
        } else {
            campaign.consumedTimes++
        }
        campaignRepository.save(campaign)
        return campaign.active
    }
}
