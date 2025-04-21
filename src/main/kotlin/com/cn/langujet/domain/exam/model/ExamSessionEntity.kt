package com.cn.langujet.domain.exam.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamSessionState
import com.cn.langujet.domain.exam.model.enums.ExamType
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_sessions")
@TypeAlias("exam_sessions")
@CompoundIndexes(
    CompoundIndex(
        name = "student_user_id_state_index",
        def = "{'studentUserId': 1, 'state': 1}",
        unique = false
    )
)
class ExamSessionEntity(
    id: Long?,
    
    @Indexed(name = "student_user_id_index")
    var studentUserId: Long,
    var examId: Long,
    var examType: ExamType,
    var examMode: ExamMode,
    var correctorType: CorrectorType,
    var state: ExamSessionState,
    var enrollDate: Date,
    var startDate: Date?,
    var endDate: Date?,
    var correctionDate: Date?,
) : HistoricalEntity(id = id) {
    constructor(
        studentUserId: Long,
        examId: Long,
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