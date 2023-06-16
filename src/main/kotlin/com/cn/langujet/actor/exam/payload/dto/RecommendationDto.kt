package com.cn.langujet.actor.exam.payload.dto

import com.cn.langujet.domain.exam.model.nested.Recommendation

data class RecommendationDto(
    val id: String?,
    val suggestionId: String,
    val content: String,
    val improvement: String,
) {
    constructor(recommendation: Recommendation) : this(
        recommendation.id,
        recommendation.suggestionId,
        recommendation.content,
        recommendation.improvement,
    )
}