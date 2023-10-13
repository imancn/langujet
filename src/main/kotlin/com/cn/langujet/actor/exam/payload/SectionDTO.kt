package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.exam.model.SectionType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Dto representation of a Section")
data class SectionDTO(
    var id: String?,
    var examId: String,
    var header: String,
    var order: Int,
    var sectionType: SectionType,
    var parts: List<PartDTO>,
    var time: Long
) {
    constructor(section: Section) : this(
        section.id,
        section.examId,
        section.header,
        section.order,
        section.sectionType,
        section.parts.map { PartDTO.from(it) }, // assuming there's a method to convert Part to PartDTO
        section.time
    )
}