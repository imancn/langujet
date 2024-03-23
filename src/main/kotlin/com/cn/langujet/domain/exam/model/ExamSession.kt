package com.cn.langujet.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_sessions")
data class ExamSession(
    @Id var id: String?,
    
    var studentId: String,
    var examId: String,
    val examVariantId: String,
    val sectionOrders: List<Int>,
    var state: ExamSessionState,
    var enrollDate: Date,
    var startDate: Date?,
    var endDate: Date?,
    var correctionDate: Date?,
) {
    constructor(
        studentId: String,
        examId: String,
        examVariantId: String,
        sectionOrders: List<Int>,
        enrollDate: Date
    ) : this(
        null,
        studentId,
        examId,
        examVariantId,
        sectionOrders,
        ExamSessionState.ENROLLED,
        enrollDate,
        null,
        null,
        null,
    )
}