package com.cn.langujet.domain.exam.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@CompoundIndexes(
    CompoundIndex(
        name = "exam_id_section_id_index",
        def = "{'examId': -1, 'sectionId': 1}",
        unique = false
    ),
    CompoundIndex(
        name = "exam_id_section_id_part_id_question_id_index",
        def = "{'examId': -1, 'sectionId': 1, 'partId': 1, 'questionId': 1}",
        unique = false
    )
)
@Document(collection = "exam_contents")
@TypeAlias("exam_contents")
class ExamContentEntity(
    id: Long? = null,
    @Indexed(name = "exam_id_index", unique = false)
    var examId: Long,
    var sectionId: Int?,
    var partId: Int?,
    var questionId: Int?,
    @Indexed(name = "unique_file_id_index", unique = true)
    var fileId: Long,
) : HistoricalEntity(id = id)
