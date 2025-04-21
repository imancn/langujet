package com.cn.langujet.domain.exam.model.section

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.domain.exam.model.enums.SectionType
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@CompoundIndexes(
    CompoundIndex(
        name = "unique_sections_index",
        def = "{'examId': -1, 'order': 1}",
        unique = true
    )
)
@Document(collection = "sections")
@TypeAlias("sections")
class SectionEntity(
    id: Long?,
    @Indexed(name = "exam_id_desc_index", direction = IndexDirection.DESCENDING)
    var examId: Long,
    var header: String,
    var order: Int,
    var sectionType: SectionType,
    var time: Long
) : HistoricalEntity(id = id)
