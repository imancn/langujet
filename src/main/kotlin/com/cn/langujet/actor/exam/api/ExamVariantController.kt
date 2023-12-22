package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamVariantCreateRequest
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.ExamVariantEntity
import com.cn.langujet.domain.exam.service.ExamVariantService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Validated
class ExamVariantController(private val examVariantService: ExamVariantService) {

    @PostMapping("/admin/exam-variant")
    @PreAuthorize("hasRole('ADMIN')")
    fun createExamVariant(
        @RequestBody @Valid examVariantCreateRequest: ExamVariantCreateRequest
    ): ResponseEntity<ExamVariantEntity> = toOkResponseEntity(
        examVariantService.createExamVariant(examVariantCreateRequest)
    )

    @GetMapping("/admin/exam-variant/all")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllExamVariantList(): ResponseEntity<List<ExamVariantEntity>> =
        toOkResponseEntity(examVariantService.getAllExamVariantList())
}