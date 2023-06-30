package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.dto.ExamDto
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.nested.Difficulty
import com.cn.langujet.domain.exam.model.nested.Price
import com.cn.langujet.domain.exam.service.ExamService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/exams")
@Validated
class ExamController(private val examService: ExamService) {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createExam(
        @RequestParam @NotBlank name: String?,
        @RequestParam @NotBlank description: String?,
        @RequestParam @NotNull sectionsNumber: Int?,
        @RequestParam @NotNull questionNumber: Int?,
        @RequestParam @NotNull examDuration: Long?,
        @RequestParam @NotNull difficulty: Difficulty?,
        @RequestParam @NotNull priceValue: Double?,
        @RequestParam @NotNull priceCurrency: Currency?,
    ): ResponseEntity<Exam> = toOkResponseEntity(
        examService.createExam(
            Exam(
                null,
                emptyList(),
                name!!,
                description!!,
                sectionsNumber!!,
                questionNumber!!,
                examDuration!!,
                difficulty!!,
                Price(
                    priceValue!!,
                    priceCurrency!!
                )
            )
        )
    )

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateExam(
        @PathVariable @NotBlank id: String?,
        @RequestBody examDto: ExamDto
    ): ResponseEntity<Exam> = toOkResponseEntity(
        examService.updateExam(
            id!!,
            examDto
        )
    )

    @GetMapping("/{id}")
    fun getExamById(@PathVariable id: String): ResponseEntity<Exam> =
        toOkResponseEntity(examService.getExamById(id))

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllExams(): ResponseEntity<List<Exam>> =
        toOkResponseEntity(examService.getAllExams())

    @GetMapping("/ filters")
    fun getAllExamsByFilters(
        @RequestParam id: String?,
        @RequestParam name: String?,
        @RequestParam sectionsNumber: Int?,
        @RequestParam questionNumber: Int?,
        @RequestParam examDuration: Long?,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "id") sortBy: String?
    ): ResponseEntity<Page<Exam>> = toOkResponseEntity(
        examService.getAllExamsByFilters(
            id,
            name,
            sectionsNumber,
            questionNumber,
            examDuration,
            PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))
        )
    )
}