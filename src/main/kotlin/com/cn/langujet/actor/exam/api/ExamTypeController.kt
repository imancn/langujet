package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamTypeRequest
import com.cn.langujet.actor.exam.payload.ExamTypeResponse
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.ExamTypeEntity
import com.cn.langujet.domain.exam.service.ExamTypeService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Validated
class ExamTypeController(private val examTypeService: ExamTypeService) {

    @PostMapping("/admin/exam-type")
    @PreAuthorize("hasRole('ADMIN')")
    fun createExamType(
        @RequestBody @Valid examTypeRequest: ExamTypeRequest
    ): ResponseEntity<ExamTypeEntity> = toOkResponseEntity(
        examTypeService.createExamType(examTypeRequest)
    )

    @PostMapping("/admin/exam-type/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateExamType(
        @PathVariable @NotBlank id: String?,
        @RequestBody examTypeRequest: ExamTypeRequest
    ): ResponseEntity<ExamTypeEntity> = toOkResponseEntity(
        examTypeService.updateExamType(
            id!!,
            examTypeRequest
        )
    )

    @GetMapping("/admin/exam-type/all")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllExamTypeList(): ResponseEntity<List<ExamTypeEntity>> =
        toOkResponseEntity(examTypeService.getAllExamTypeList())

    @GetMapping("student/exam-type/all/available")
    fun getAllAvailableExamTypeList(): ResponseEntity<List<ExamTypeResponse>> =
        toOkResponseEntity(examTypeService.getAllAvailableExamTypeList())
}