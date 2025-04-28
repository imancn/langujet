package com.cn.langujet.domain.campaign

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository

interface CampaignRepository : HistoricalMongoRepository<CampaignEntity> {
    fun findByCodeAndActive(code: String, active: Boolean): CampaignEntity?
    fun findAllByActive(active: Boolean): MutableList<CampaignEntity>
    fun existsByActiveAndCode(active: Boolean, code: String): Boolean
}
