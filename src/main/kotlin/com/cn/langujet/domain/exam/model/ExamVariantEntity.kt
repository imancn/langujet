package com.cn.langujet.domain.exam.model

import com.cn.langujet.domain.correction.model.CorrectionType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exam_variants")
class ExamVariantEntity (
    @Id
    var id: String?,
    var examType: ExamType,
    var sectionTypes: List<SectionType>,
    var correctionType: CorrectionType,
)
