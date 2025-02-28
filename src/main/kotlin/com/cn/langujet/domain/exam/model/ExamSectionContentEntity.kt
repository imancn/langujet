package com.cn.langujet.domain.exam.model

import com.cn.langujet.application.shared.LogEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@CompoundIndexes(
    CompoundIndex(
        name = "exam_id_section_order_index",
        def = "{'examId': -1, 'sectionOrder': 1}",
        unique = false
    )
)
@Document(collection = "exam_section_contents")
@TypeAlias("exam_section_contents")
class ExamSectionContentEntity(
    @Id
    var id: String?,
    var examId: String,
    var sectionOrder: Int,
    @Indexed(name = "file_id_index", unique = true)
    var fileId: String,
): LogEntity()
