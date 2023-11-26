package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.exam.model.SectionType
import com.fasterxml.jackson.annotation.JsonInclude

data class SectionDTO(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String? = null,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var examId: String? = null,
    var header: String? = null,
    var order: Int? = null,
    var sectionType: SectionType? = null,
    var parts: List<PartDTO>? = null,
    var time: Long? = null
) {
    fun toSection(): Section {
        return Section(
            this.id,
            this.examId!!,
            this.header!!,
            this.order!!,
            this.sectionType!!,
            this.parts?.map { it.toPart() }!!,
            this.time!!
        )
    }

    constructor(section: Section) : this(
        section.id,
        section.examId,
        section.header,
        section.order,
        section.sectionType,
        section.parts.map { PartDTO.from(it) },
        section.time
    )
}