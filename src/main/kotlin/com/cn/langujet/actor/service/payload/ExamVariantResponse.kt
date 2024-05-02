package com.cn.langujet.actor.service.payload

import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.ExamVariantEntity
import com.cn.langujet.domain.exam.model.SectionType

class ExamVariantResponse (
    var examType: ExamType,
    var sectionTypes: List<SectionType>,
    var correctorType: CorrectorType,
) {

    companion object {
        fun ExamVariantEntity.toExamTypeResponse(): ExamVariantResponse {
            return ExamVariantResponse(
                examType,
                sectionTypes,
                correctorType
            )
        }
    }
}