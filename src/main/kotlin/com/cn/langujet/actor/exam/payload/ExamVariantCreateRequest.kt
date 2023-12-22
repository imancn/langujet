package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.correction.model.CorrectionType
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.ExamVariantEntity
import com.cn.langujet.domain.exam.model.SectionType
import jakarta.validation.constraints.NotNull

class ExamVariantCreateRequest(
    @field:NotNull var examType: ExamType? = null,
    @field:NotNull var sectionTypes: List<SectionType>? = null,
    @field:NotNull var correctionType: CorrectionType? = null,
) {
    fun toEntity(): ExamVariantEntity {
        return ExamVariantEntity(
            null,
            examType!!,
            sectionTypes!!,
            correctionType!!,
        )
    }
}
