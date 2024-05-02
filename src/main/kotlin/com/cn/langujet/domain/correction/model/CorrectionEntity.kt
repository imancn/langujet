package com.cn.langujet.domain.correction.model

import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "corrections")
class CorrectionEntity(
    @Id var id: String?,
    var correctorType: CorrectorType,
    var status: CorrectionStatus,
    var examSessionId: String,
    var examType: ExamType,
    var sectionOrder: Int,
    var sectionType: SectionType,
    var correctorUserId: String?,
    var createdDate: Date,
    var updatedDate: Date
) {
    constructor(
        correctorType: CorrectorType,
        status: CorrectionStatus,
        examSessionId: String,
        examType: ExamType,
        sectionOrder: Int,
        sectionType: SectionType
    ) : this(
        id = null,
        correctorType = correctorType,
        status = status,
        examSessionId = examSessionId,
        examType = examType,
        sectionOrder = sectionOrder,
        sectionType = sectionType,
        correctorUserId = null,
        createdDate = Date(System.currentTimeMillis()),
        updatedDate = Date(System.currentTimeMillis()),
    )
    
    fun refreshUpdatedDate() {
        this.updatedDate = Date(System.currentTimeMillis())
    }
}