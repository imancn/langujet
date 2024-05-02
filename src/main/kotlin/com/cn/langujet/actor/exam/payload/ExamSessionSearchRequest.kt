package com.cn.langujet.actor.exam.payload

import com.cn.langujet.actor.util.models.DateInterval
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamSessionState
import com.cn.langujet.domain.exam.model.ExamType
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

data class ExamSessionSearchRequest(
    val states: List<ExamSessionState>?,
    val examTypes: List<ExamType>?,
    val examName: String?,
    val correctorTypes: List<CorrectorType>?,
    val startDateInterval: DateInterval?,
    val correctionDateInterval: DateInterval?,
    @field:Positive val pageSize: Int,
    @field:PositiveOrZero val pageNumber: Int,
)