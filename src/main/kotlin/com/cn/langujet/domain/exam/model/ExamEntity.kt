package com.cn.langujet.domain.exam.model

import com.cn.langujet.application.shared.entity.HistoricalEntity
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exams")
@TypeAlias("exams")
@CompoundIndexes(
    CompoundIndex(
        name = "type_mode_active_index",
        def = "{'type': 1, 'mode': 1, 'active': 1}",
        unique = false
    )
)
data class ExamEntity(
    @Id var id: String?,
    var type: ExamType,
    var mode: ExamMode,
    @TextIndexed
    @Indexed(name = "name_index")
    var name: String,
    var description: String,
    var sectionsNumber: Int,
    var questionNumber: Int,
    var examDuration: Long, // Seconds
    @Indexed(name = "active_index")
    var active: Boolean
): HistoricalEntity()