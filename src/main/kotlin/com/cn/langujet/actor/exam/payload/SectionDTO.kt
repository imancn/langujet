package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.section.SectionEntity
import com.cn.langujet.domain.exam.model.enums.SectionType
import com.fasterxml.jackson.annotation.JsonInclude

data class SectionDTO(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String? = null,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var examId: String? = null,
    var header: String? = null,
    var sectionOrder: Int? = null,
    var sectionType: SectionType? = null,
    var parts: List<PartDTO>? = null,
    var time: Long? = null
) {
    fun toSection(): SectionEntity {
        return SectionEntity(
            this.id,
            this.examId!!,
            this.header!!,
            this.sectionOrder!!,
            this.sectionType!!,
            this.parts?.map { it.toPart() }!!,
            this.time!!
        )
    }

    constructor(section: SectionEntity) : this(
        section.id,
        section.examId,
        section.header,
        section.order,
        section.sectionType,
        section.parts.map { PartDTO.from(it) },
        section.time
    )
}