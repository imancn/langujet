package com.cn.langujet.domain.campaign

import org.springframework.data.mongodb.repository.MongoRepository

interface CampaignRepository : MongoRepository<CampaignEntity, String> {
    fun findByCodeAndActive(code: String, active: Boolean): CampaignEntity?
    fun findAllByActive(active: Boolean): MutableList<CampaignEntity>
    fun existsByActiveAndCode(active: Boolean, code: String): Boolean
}
