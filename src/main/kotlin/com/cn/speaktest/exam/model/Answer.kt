package com.cn.speaktest.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "answers")
data class Answer(
    @Id
    var id: String?,
    var userId: String,
    var audioUrl: String
)
