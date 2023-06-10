package com.cn.speaktest.domain.exam.model

import com.cn.speaktest.actor.exam.payload.dto.SuggestionDto
import com.cn.speaktest.domain.exam.model.nested.Recommendation
import com.cn.speaktest.domain.exam.model.nested.Score
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "suggestions")
data class Suggestion(
    @Id var id: String?,
    var examSectionId: String,
    @DBRef var score: Score,
    @DBRef var recommendations: List<Recommendation>,
    var overallRecommendation: String,
) {
    constructor(suggestionDto: SuggestionDto) : this(
        suggestionDto.id,
        suggestionDto.examSectionId,
        Score(suggestionDto.score),
        suggestionDto.recommendations.map { Recommendation(it) },
        suggestionDto.overallRecommendation,
    )
}
