package com.cn.langujet.domain.exam.model

import com.cn.langujet.application.shared.HistoricalEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sections")
@TypeAlias("sections")
class SectionEntity(
    @Id
    var id: String?,
    var examId: String,
    var header: String,
    var order: Int,
    var sectionType: SectionType,
    var parts: List<Part>,
    var time: Long
): HistoricalEntity()
