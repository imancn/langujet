package com.cn.langujet.domain.campaign

import com.cn.langujet.application.shared.entity.HistoricalEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "campaigns")
@TypeAlias("campaigns")
data class CampaignEntity(
    @Id var id: String? = null,
    var name: String,
    var code: String,
    var amount: Double,
    var percentage: Int,
    var usageLimit: Int,
    var usedTimes: Int = 0,
    var active: Boolean = false,
    var tag: String? = null,
    var description: String? = null,
): HistoricalEntity()
