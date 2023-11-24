package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.ExamTypeEntity
import com.cn.langujet.domain.exam.model.SectionType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class ExamTypeRequest (
    @field:NotBlank var name: String? = null,
    @field:NotNull var examType: ExamType? = null,
    var sectionType: SectionType? = null,
    @field:NotNull var price: Double? = null,
    @field:NotNull var order: Int? = null,
    @field:NotNull var active: Boolean? = null,
) {
    fun toEntity(): ExamTypeEntity {
        return ExamTypeEntity(
            null,
            name!!,
            examType!!,
            sectionType,
            price!!,
            order!!,
            active!!,
        )
    }
}
