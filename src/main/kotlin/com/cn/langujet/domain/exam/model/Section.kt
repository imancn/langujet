package com.cn.langujet.domain.exam.model

import com.cn.langujet.actor.exam.payload.dto.SectionDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sections")
class Section(
    @Id var id: String?,
    var examId: String,
    var name: String,
    var order: Int,
) {
    constructor(section: SectionDto) : this(
        section.id,
        section.examId,
        section.name!!,
        section.order!!,
    )
}