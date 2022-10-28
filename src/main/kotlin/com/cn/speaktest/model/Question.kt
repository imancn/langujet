package com.cn.speaktest.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "questions")
data class Question(
    @Id
    var id: String?,
    var section: Section,
    var topic: String,
    var order: Int,
    var text: String,
    var usageNumber: Int
) {
    enum class Section(val num: Int) {
        GENERAL_DISCUSSION(1),
        MONOLOGUE(2),
        DETAILED_DISCUSSION(3)
    }
}
