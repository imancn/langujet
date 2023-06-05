package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.Score

data class ScoreDto(
    val id: String?,
    val name: String,
    val score: Int,
    val suggestionId: String,
    val subScores: List<ScoreDto>?,
) {
    constructor(score: Score) : this(
        score.id,
        score.name,
        score.score,
        score.suggestionId,
        score.subScores?.map { ScoreDto(it) },
    )
}