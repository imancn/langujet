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
    @Id var id: String?,

    @DBRef var exam: Exam,
    @DBRef var student: Student,
    @DBRef var professor: Professor,
    @DBRef var examSections: List<ExamSection>?,

    var requestDate: Date,
    var startDate: Date?,
    var endDate: Date?,
    var rateDate: Date?,

    var isStarted: Boolean = false,
    var isFinished: Boolean = false,
    var isRated: Boolean = false,
) {
    constructor(exam: Exam, student: Student, professor: Professor, requestDate: Date) : this(
        null,
        exam,
        student,
        professor,
        null,
        requestDate,
        null,
        null,
        null,
    )

    constructor(
        examSession: ExamSessionDto, student: Student, professor: Professor, exam: Exam, examSectionList: List<ExamSection>
    ) : this(
        examSession.id,
        exam,
        student,
        professor,
        examSectionList,
        examSession.requestDate,
        examSession.startDate,
        examSession.endDate,
        examSession.rateDate,
        examSession.isStarted,
        examSession.isFinished,
        examSession.isRated,
    )
}