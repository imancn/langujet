package com.cn.langujet.actor.service.payload

import com.cn.langujet.domain.correction.model.CorrectionType
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.ExamVariantEntity
import com.cn.langujet.domain.exam.model.SectionType

class ExamVariantResponse (
    var examType: ExamType,
    var sectionTypes: List<SectionType>,
    var correctionType: CorrectionType,
) {

    companion object {
        fun ExamVariantEntity.toExamTypeResponse(): ExamVariantResponse {
            return ExamVariantResponse(
                examType,
                sectionTypes,
                correctionType
            )
        }
    }
}