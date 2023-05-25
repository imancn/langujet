package com.cn.speaktest.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "suggestions")
data class Suggestion(
    @Id
    var id: String?,

    @DBRef
    var exam: Exam,
    @DBRef
    var score: Score,
    @DBRef
    var issues: List<Issue>,

    var overallRecommendation: String,
)
