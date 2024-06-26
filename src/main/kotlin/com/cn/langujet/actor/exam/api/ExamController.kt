package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamDTO
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.service.ExamService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Validated
class ExamController(private val examService: ExamService) {
    
    @PostMapping("/admin/exams")
    @PreAuthorize("hasRole('ADMIN')")
    fun createExam(
        @RequestBody @Valid exam: ExamDTO
    ): ResponseEntity<ExamDTO> = toOkResponseEntity(
        examService.createExam(exam)
    )
    
    @PostMapping("/admin/exams/update")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateExam(
        @RequestBody exam: ExamDTO
    ): ResponseEntity<ExamDTO> = toOkResponseEntity(
        examService.updateExam(exam)
    )
    
    @GetMapping("/admin/exams/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getExamById(@PathVariable id: String): ResponseEntity<ExamDTO> =
        toOkResponseEntity(ExamDTO(examService.getExamById(id)))
    
    @GetMapping("/admin/exams")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllExams(): ResponseEntity<List<ExamDTO>> = toOkResponseEntity(examService.getAllExams())
    
    @GetMapping("/admin/exams/by-name")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllExamsByName(
        @RequestParam @NotBlank name: String?,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "0") pageNumber: Int,
    ): ResponseEntity<CustomPage<ExamDTO>> = toOkResponseEntity(
        examService.getAllExamsByName(
            name!!, PageRequest.of(pageNumber, pageSize)
        )
    )
}