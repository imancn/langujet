package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.service.ExamService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/exams")
@Validated
class ExamController(private val examService: ExamService) {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createExam(
        @RequestParam @NotBlank name: String?,
        @RequestParam @NotNull type: ExamType?,
        @RequestParam @NotBlank description: String?,
        @RequestParam @NotNull sectionsNumber: Int?,
        @RequestParam @NotNull questionNumber: Int?,
        @RequestParam @NotNull examDuration: Long?,
    ): ResponseEntity<Exam> = toOkResponseEntity(
        examService.createExam(
            Exam(
                null,
                type!!,
                name!!,
                description!!,
                sectionsNumber!!,
                questionNumber!!,
                examDuration!!
            )
        )
    )

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateExam(
        @PathVariable @NotBlank id: String?,
        @RequestBody examResponse: Exam
    ): ResponseEntity<Exam> = toOkResponseEntity(
        examService.updateExam(
            id!!,
            examResponse
        )
    )

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getExamById(@PathVariable id: String): ResponseEntity<Exam> =
        toOkResponseEntity(examService.getExamById(id))

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllExams(): ResponseEntity<List<Exam>> =
        toOkResponseEntity(examService.getAllExams())

    @GetMapping("/name")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllExamsByName(
        @RequestParam @NotBlank name: String?,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "0") pageNumber: Int,
    ): ResponseEntity<Page<Exam>> = toOkResponseEntity(
        examService.getAllExamsByName(
            name!!,
            PageRequest.of(pageNumber, pageSize)
        )
    )
}