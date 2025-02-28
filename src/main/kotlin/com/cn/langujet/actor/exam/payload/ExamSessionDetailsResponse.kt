package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.repository.dto.SectionMetaDTO

class ExamSessionDetailsResponse(sections: List<SectionMetaDTO>) {
    val sections: List<ExamSessionSections> = sections.map {
        ExamSessionSections(
            it.order,
            it.sectionType,
            it.time
        )
    }
}

data class ExamSessionSections(
    var order: Int,
    var sectionType: SectionType,
    var time: Long
)