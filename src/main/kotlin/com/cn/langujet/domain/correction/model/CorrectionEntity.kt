package com.cn.langujet.domain.correction.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "corrections")
class CorrectionEntity(
    @Id var id: String?,
    var type: CorrectionType,
    var status: CorrectionStatus,
    var examSessionId: String,
    var sectionOrder: Int,
    var professorId: String?,
    var createdDate: Date,
    var updatedDate: Date
) {
    constructor(type: CorrectionType, status: CorrectionStatus, examSessionId: String, sectionOrder: Int) : this(
        id = null,
        type = type,
        status = status,
        examSessionId = examSessionId,
        sectionOrder = sectionOrder,
        professorId = null,
        createdDate = Date(System.currentTimeMillis()),
        updatedDate = Date(System.currentTimeMillis()),
    )
    
    fun refreshUpdatedDate() {
        this.updatedDate = Date(System.currentTimeMillis())
    }
}