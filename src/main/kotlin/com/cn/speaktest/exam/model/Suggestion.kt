package com.cn.speaktest.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "suggestions")
data class Suggestion(
    @Id
    var id: String?,
    var grammar: String,
    var fluency: String,
    var vocabulary: String,
    var pronunciation: String
)
