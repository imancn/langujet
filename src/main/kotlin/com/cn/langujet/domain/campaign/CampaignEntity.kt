package com.cn.langujet.domain.campaign

import com.cn.langujet.application.shared.HistoricalEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "campaigns")
@TypeAlias("campaigns")
data class CampaignEntity(
    @Id var id: String? = null,
    var name: String,
    @Indexed(unique = true)
    var code: String,
    var amount: Double,
    var percentage: Int,
    var limit: Int,
    var consumedTimes: Int,
    var active: Boolean = false,
    var tag: String? = null,
    var description: String? = null,
): HistoricalEntity()
