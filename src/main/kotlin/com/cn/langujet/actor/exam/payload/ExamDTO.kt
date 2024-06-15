package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamMode
import com.cn.langujet.domain.exam.model.ExamType
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
    constructor(exam: Exam): this(
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

    fun toExam(): Exam {
        return Exam(
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