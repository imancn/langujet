package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.ExamRequest
import java.util.*

data class ExamRequestDto(
    val examInfoId: String?,
    val studentId: String?,
    val date: Date,
) {
    constructor(examRequest: ExamRequest) : this(
        examRequest.examInfo.id,
        examRequest.student.id,
        examRequest.date
    )
}