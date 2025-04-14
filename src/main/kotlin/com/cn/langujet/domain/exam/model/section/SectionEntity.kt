package com.cn.langujet.domain.exam.model.section

import com.cn.langujet.application.arch.models.Historical
import com.cn.langujet.domain.exam.model.enums.SectionType
import org.springframework.data.annotation.Id
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
data class SectionEntity(
    @Id
    var id: String?,
    @Indexed(name = "exam_id_index", direction = IndexDirection.DESCENDING)
    var examId: String,
    var header: String,
    var order: Int,
    var sectionType: SectionType,
    var time: Long
) : Historical()
