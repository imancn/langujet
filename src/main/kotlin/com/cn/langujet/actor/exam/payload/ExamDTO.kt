package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ExamDTO(
    var id: String? = null,
    @field:NotNull var examType: ExamType? = null,
    @field:NotBlank var name: String? = null,
    @field:NotBlank var description: String? = null,
    @field:NotNull var sectionsNumber: Int? = null,
    @field:NotNull var questionNumber: Int? = null,
    @field:NotNull var examDuration: Long? = null, // Milliseconds
) {
    constructor(exam: Exam): this(
        exam.id,
        exam.examType,
        exam.name,
        exam.description,
        exam.sectionsNumber,
        exam.questionNumber,
        exam.examDuration
    )

    fun toExam(): Exam {
        return Exam(
            this.id,
            this.examType!!,
            this.name!!,
            this.description!!,
            this.sectionsNumber!!,
            this.questionNumber!!,
            this.examDuration!!
        )
    }
}