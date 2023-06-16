package com.cn.langujet.domain.exam.model.nested

import com.cn.langujet.actor.exam.payload.dto.RecommendationDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "issues")
data class Recommendation(
    @Id var id: String?,
    @DBRef var suggestionId: String,
    var content: String,
    var improvement: String
) {
    constructor(recommendationDto: RecommendationDto) : this(
        recommendationDto.id,
        recommendationDto.suggestionId,
        recommendationDto.content,
        recommendationDto.improvement,
    )
}