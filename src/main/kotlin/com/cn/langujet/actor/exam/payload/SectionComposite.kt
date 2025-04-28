package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.model.section.SectionEntity
import com.cn.langujet.domain.exam.model.section.part.PartEntity
import com.cn.langujet.domain.exam.model.section.part.questions.QuestionEntity
import com.fasterxml.jackson.annotation.JsonInclude

data class SectionComposite(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: Long? = null,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var examId: Long?,
    var header: String,
    var sectionOrder: Int,
    var sectionType: SectionType,
    var parts: List<PartComposite>,
    var time: Long
) {
    fun toSection(): SectionEntity {
        return SectionEntity(
            this.id,
            this.examId!!,
            this.header,
            this.sectionOrder,
            this.sectionType,
            this.time
        )
    }
    
    constructor(section: SectionEntity, parts: List<PartEntity>, questions: List<QuestionEntity>) : this(
        section.id,
        section.examId,
        section.header,
        section.order,
        section.sectionType,
        questions.groupBy { question ->
            question.partId
        }.mapNotNull { (partId, questions) ->
            parts.find { part -> part.id == partId }?.let { PartComposite.from(it, questions) }
        },
        section.time
    )
}