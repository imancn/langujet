package com.cn.langujet.domain.exam.model

import com.cn.langujet.domain.correction.model.CorrectorType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_sessions")
@TypeAlias("exam_sessions")
data class ExamSession(
    @Id var id: String?,
    
    var studentUserId: String,
    var examId: String,
    var examType: ExamType,
    var examMode: ExamMode,
    var correctorType: CorrectorType,
    var state: ExamSessionState,
    var enrollDate: Date,
    var startDate: Date?,
    var endDate: Date?,
    var correctionDate: Date?,
) {
    constructor(
        studentUserId: String,
        examId: String,
        examType: ExamType,
        examMode: ExamMode,
        correctorType: CorrectorType,
        enrollDate: Date
    ) : this(
        null,
        studentUserId,
        examId,
        examType,
        examMode,
        correctorType,
        ExamSessionState.ENROLLED,
        enrollDate,
        null,
        null,
        null,
    )
}