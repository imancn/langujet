package com.cn.langujet.domain.result.model

import com.cn.langujet.domain.exam.model.SectionType
import nonapi.io.github.classgraph.json.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "section_results")
class SectionResult(
    @Id
    var id: String?,
    var resultId: String,
    var sectionOrder: Int,
    var sectionType: SectionType,
    var correctIssuesCount: Int,
    var score: Double,
    var recommendation: String?,
)