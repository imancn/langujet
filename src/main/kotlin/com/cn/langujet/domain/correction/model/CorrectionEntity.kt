package com.cn.langujet.domain.correction.model

import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "corrections")
class CorrectionEntity(
    @Id var id: String?,
    var type: CorrectionType,
    var status: CorrectionStatus,
    var examSessionId: String,
    var examType: ExamType,
    var sectionOrder: Int,
    var sectionType: SectionType,
    var professorUserId: String?,
    var createdDate: Date,
    var updatedDate: Date
) {
    constructor(
        type: CorrectionType,
        status: CorrectionStatus,
        examSessionId: String,
        examType: ExamType,
        sectionOrder: Int,
        sectionType: SectionType
    ) : this(
        id = null,
        type = type,
        status = status,
        examSessionId = examSessionId,
        examType = examType,
        sectionOrder = sectionOrder,
        sectionType = sectionType,
        professorUserId = null,
        createdDate = Date(System.currentTimeMillis()),
        updatedDate = Date(System.currentTimeMillis()),
    )
    
    fun refreshUpdatedDate() {
        this.updatedDate = Date(System.currentTimeMillis())
    }
}