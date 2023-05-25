package com.cn.speaktest.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "scores")
data class Score(
    @Id
    val id: String?,

    @DBRef
    val detailedScores: List<Score>,

    val name: String,
    val score: Int,
)
