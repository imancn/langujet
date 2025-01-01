package com.cn.langujet.actor.campaign

data class CreateCampaignRequest(
    val name: String,
    val code: String,
    val amount: Double,
    val tag: String,
    val percentage: Int,
    val limit: Int,
    val description: String?
)