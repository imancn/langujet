package com.cn.langujet.domain.result.model

import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.SectionType
import nonapi.io.github.classgraph.json.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "section_results")
@TypeAlias("section_results")
class SectionResultEntity(
    @Id
    var id: String?,
    var resultId: String,
    var examSessionId: String,
    var sectionOrder: Int,
    var sectionType: SectionType,
    var correctorType: CorrectorType,
    var correctorUserId: String?,
    var status: CorrectionStatus,
    var correctIssuesCount: Int?,
    var score: Double?,
    var recommendation: String?,
    var createdDate: Date,
    var updatedDate: Date
)