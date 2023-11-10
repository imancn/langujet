package com.cn.langujet.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_sessions")
data class ExamSession(
    @Id var id: String?,

    var studentId: String,
    var professorId: String?,

    var examId: String,
    var sectionId: String?,

    val sectionOrders: List<Int>,
    var state: ExamSessionState,

    var enrollDate: Date,
    var startDate: Date?,
    var endDate: Date?,
    var rateDate: Date?,
) {
    constructor(
        studentId: String,
        professorId: String?,
        examId: String,
        sectionId: String?,
        sectionOrders: List<Int>,
        requestDate: Date
    ) : this(
        null,
        studentId,
        professorId,
        examId,
        sectionId,
        sectionOrders,
        ExamSessionState.ENROLLED,
        requestDate,
        null,
        null,
        null,
    )
}