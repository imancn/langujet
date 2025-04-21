package com.cn.langujet.domain.result.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.enums.SectionType
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
    id: Long?,
    @Indexed(name = "result_id_index", unique = false)
    var resultId: Long,
    var examSessionId: Long,
    var sectionOrder: Int,
    var sectionType: SectionType,
    var correctorType: CorrectorType,
    var correctorUserId: Long?,
    var status: CorrectionStatus,
    var correctIssuesCount: Int?,
    var score: Double?,
    var recommendation: String?,
    var attachmentFileId: Long?
) : HistoricalEntity(id = id)