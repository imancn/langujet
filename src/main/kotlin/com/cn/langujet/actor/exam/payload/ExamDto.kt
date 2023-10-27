package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamType

data class ExamDto(
    val id: String?,
    val examType: ExamType,
    val name: String?,
    val description: String?,
    val sectionsNumber: Int?,
    val questionNumber: Int?,
    val examDuration: Long?, // Milliseconds
) {
    constructor(exam: Exam) : this(
        exam.id,
        exam.examType,
        exam.name,
        exam.description,
        exam.sectionsNumber,
        exam.questionNumber,
        exam.examDuration
    )
}