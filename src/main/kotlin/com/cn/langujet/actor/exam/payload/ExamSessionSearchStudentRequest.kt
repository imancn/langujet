package com.cn.langujet.actor.exam.payload

import com.cn.langujet.actor.util.models.DateInterval
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.enums.ExamSessionState
import com.cn.langujet.domain.exam.model.enums.ExamType
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

data class ExamSessionSearchStudentRequest(
    val states: List<ExamSessionState>?,
    val examTypes: List<ExamType>?,
    val examName: String?,
    val correctorTypes: List<CorrectorType>?,
    val startDateInterval: DateInterval?,
    val correctionDateInterval: DateInterval?,
    @field:Positive val pageSize: Int,
    @field:PositiveOrZero val pageNumber: Int,
)