package com.cn.langujet.domain.exam.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
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
    ),
    CompoundIndex(
        name = "exam_id_section_order_part_order_question_order_index",
        def = "{'examId': -1, 'sectionOrder': 1, 'partOrder': 1, 'questionOrder': 1}",
        unique = false
    )
)
@Document(collection = "exam_contents")
@TypeAlias("exam_contents")
class ExamContentEntity(
    id: Long? = null,
    @Indexed(name = "exam_id_index", unique = false)
    var examId: String,
    var sectionOrder: Int?,
    var partOrder: Int?,
    var questionOrder: Int?,
    @Indexed(name = "file_id_index", unique = true)
    var fileId: String,
) : HistoricalEntity(id = id)
