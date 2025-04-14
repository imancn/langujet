package com.cn.langujet.domain.result.model

import com.cn.langujet.application.arch.models.Historical
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.enums.SectionType
import nonapi.io.github.classgraph.json.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@CompoundIndexes(
    CompoundIndex(
        name = "unique_section_results_index",
        def = "{'examSessionId': -1, 'sectionOrder': 1}",
        unique = true
    )
)
@Document(collection = "section_results")
@TypeAlias("section_results")
class SectionResultEntity(
    @Id
    var id: String?,
    @Indexed(name = "result_id_index", unique = false)
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
    var attachmentFileId: String?
) : Historical()