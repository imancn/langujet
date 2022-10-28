package com.cn.speaktest.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "suggestions")
data class Suggestion(
    @Id
    var id: String?,
    var grammer: String,
    var fulency: String,
    var vocabulary: String,
)
