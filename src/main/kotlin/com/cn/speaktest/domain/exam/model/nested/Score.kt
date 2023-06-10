package com.cn.speaktest.domain.exam.model.nested

import com.cn.speaktest.actor.exam.payload.dto.ScoreDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "scores")
data class Score(
    @Id val id: String?,
    val name: String,
    val score: Int,
    var suggestionId: String,
    @DBRef val subScores: List<Score>?,
) {
    constructor(scoreDto: ScoreDto) : this(
        scoreDto.id,
        scoreDto.name,
        scoreDto.score,
        scoreDto.suggestionId,
        scoreDto.subScores?.map { Score(it) },
    )
}
