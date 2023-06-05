package com.cn.speaktest.domain.exam.model

import com.cn.speaktest.actor.exam.payload.dto.ExamSessionDto
import com.cn.speaktest.domain.professor.Professor
import com.cn.speaktest.domain.student.model.Student
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "exam_sessions")
data class ExamSession(
    @Id
    var id: String?,

    @DBRef
    var examMeta: ExamMeta,
    @DBRef
    var student: Student,
    @DBRef
    var professor: Professor,
    @DBRef
    var examSections: List<ExamSection>?,

    var requestDate: Date,
    var startDate: Date?,
    var endDate: Date?,
    var rateDate: Date?,

    var isStarted: Boolean = false,
    var isFinished: Boolean = false,
    var isRated: Boolean = false,
) {
    constructor(examMeta: ExamMeta, student: Student, professor: Professor, requestDate: Date) : this(
        null,
        examMeta,
        student,
        professor,
        null,
        requestDate,
        null,
        null,
        null,
    )

    constructor(examSession: ExamSessionDto, student: Student, professor: Professor) : this(
        examSession.id,
        ExamMeta(examSession.examInfo),
        student,
        professor,
        examSession.examSections?.map { ExamSection(it) },
        examSession.requestDate,
        examSession.startDate,
        examSession.endDate,
        examSession.rateDate,
        examSession.isStarted,
        examSession.isFinished,
        examSession.isRated,
    )
}