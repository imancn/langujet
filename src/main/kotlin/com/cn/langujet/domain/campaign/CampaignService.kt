package com.cn.langujet.domain.campaign

import com.cn.langujet.actor.campaign.CreateCampaignRequest
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull
import kotlin.jvm.optionals.toList

@Service
class CampaignService(
    override var repository: CampaignRepository
) : HistoricalEntityService<CampaignRepository, CampaignEntity>() {
    fun createCampaign(request: CreateCampaignRequest): CampaignEntity {
        if (request.percentage > 100 || request.percentage < 1) throw UnprocessableException("Percentage must be between 1% to 100%")
        if (request.amount < 0) throw UnprocessableException("Amount must not be negative")
        return create(
            CampaignEntity(
                id = null,
                name = request.name,
                code = request.code.uppercase(),
                amount = request.amount,
                percentage = request.percentage,
                usageLimit = request.limit,
                tag = request.tag,
                description = request.description
            )
        )
    }
    
    fun changeCampaignActiveFlag(campaignId: Long, active: Boolean): CampaignEntity {
        val campaign = getForUpdate(campaignId)
        if (active) {
            if (repository.existsByActiveAndCode(true, campaign.code)) {
                throw UnprocessableException("Another [${campaign.code}] campaign active flag is already active")
            }
            campaign.active = true
        } else {
            campaign.active = false
        }
        return repository.save(campaign)
    }
    
    fun changeCampaignMetadata(
        campaignId: Long,
        name: String? = null,
        tag: String? = null,
        description: String? = null
    ): CampaignEntity {
        val campaign = getForUpdate(campaignId)
        name?.let { campaign.name = it }
        tag?.let { campaign.tag = it }
        description?.let { campaign.description = it }
        return repository.save(campaign)
    }
    
    fun changeUsageLimit(campaignId: Long, usageLimit: Int): CampaignEntity {
        val campaign = getForUpdate(campaignId)
        if (usageLimit <= 0) {
            throw UnprocessableException("UsageLimit must be positive")
        } else if (campaign.usedTimes > usageLimit) {
            throw UnprocessableException("UsageLimit must be bigger than than used time: ${campaign.usedTimes}")
        } else {
            campaign.usedTimes = usageLimit
        }
        return repository.save(campaign)
    }
    
    private fun getForUpdate(campaignId: Long): CampaignEntity {
        return repository.findById(campaignId).orElseThrow {
            UnprocessableException("Campaign with id $campaignId does not exist")
        }
    }
    
    fun getCampaigns(active: Boolean?, campaignId: Long?): List<CampaignEntity> {
        campaignId?.let { return repository.findById(campaignId).toList() }
        return if (active == null) {
            repository.findAll()
        } else {
            repository.findAllByActive(active)
        }
    }
    
    fun getActiveCampaignByCode(code: String): CampaignEntity? {
        return repository.findByCodeAndActive(code, true)
    }
    
    fun useCampaign(campaignId: Long): CampaignEntity? {
        val campaign = repository.findById(campaignId).getOrNull() ?: return null
        if (!campaign.active) return null
        if (campaign.usedTimes + 1 > campaign.usageLimit) {
            campaign.active = false
            repository.save(campaign)
            return null
        } else {
            campaign.usedTimes++
            return repository.save(campaign)
        }
    }
}
