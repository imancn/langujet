package com.cn.langujet.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sections")
class Section(
    @Id var id: String?,
    var examId: String,
    var header: String,
    var order: Int,
    var sectionType: SectionType,
    var parts: List<Part>,
)
