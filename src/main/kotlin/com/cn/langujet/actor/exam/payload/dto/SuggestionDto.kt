package com.cn.langujet.actor.exam.payload.dto

import com.cn.langujet.domain.exam.model.Suggestion

data class SuggestionDto(
    val id: String?,
    val examSectionId: String,
    val score: ScoreDto,
    val recommendations: List<RecommendationDto>,
    val overallRecommendation: String,
) {
    constructor(suggestion: Suggestion) : this(
        suggestion.id,
        suggestion.examSectionId,
        ScoreDto(suggestion.score),
        suggestion.recommendations.map { RecommendationDto(it) },
        suggestion.overallRecommendation,
    )
}