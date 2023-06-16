package com.cn.langujet.actor.exam.payload.dto

import com.cn.langujet.domain.exam.model.ExamRequest
import java.util.*

data class ExamRequestDto(
    val examId: String?,
    val studentId: String?,
    val date: Date,
) {
    constructor(examRequest: ExamRequest) : this(
        examRequest.exam.id,
        examRequest.student.id,
        examRequest.date
    )
}