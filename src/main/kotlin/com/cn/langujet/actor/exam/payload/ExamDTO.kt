package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.ExamEntity
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.NotBlank

data class ExamDTO(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String? = null,
    var examType: ExamType,
    var examMode: ExamMode,
    @field:NotBlank
    var name: String,
    @field:NotBlank
    var description: String,
    var sectionsNumber: Int,
    var questionNumber: Int,
    var examDuration: Long, // Seconds
    var active: Boolean, // Seconds
) {
    constructor(exam: ExamEntity): this(
        exam.id,
        exam.type,
        exam.mode,
        exam.name,
        exam.description,
        exam.sectionsNumber,
        exam.questionNumber,
        exam.examDuration,
        exam.active
    )

    fun toExam(): ExamEntity {
        return ExamEntity(
            this.id,
            this.examType,
            this.examMode,
            this.name,
            this.description,
            this.sectionsNumber,
            this.questionNumber,
            this.examDuration,
            this.active
        )
    }
}