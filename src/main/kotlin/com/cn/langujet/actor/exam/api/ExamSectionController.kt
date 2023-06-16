package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.dto.ExamSectionDto
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.ExamSection
import com.cn.langujet.domain.exam.service.ExamSectionService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/exam-sections")
class ExamSectionController(private val examSectionService: ExamSectionService) {
    @GetMapping("")
    fun getAllExamSections(): List<ExamSection> {
        return examSectionService.getAllExamSections()
    }

    @GetMapping("/{id}")
    fun getExamSectionById(@PathVariable("id") id: String): ResponseEntity<ExamSection> {
        return toOkResponseEntity(examSectionService.getExamSectionById(id))
    }

    @PostMapping("")
    fun createExamSection(@RequestBody @Valid examSectionDto: ExamSectionDto): ResponseEntity<ExamSectionDto> {
        return toOkResponseEntity(examSectionService.createExamSection(examSectionDto))
    }

    @PutMapping("/{id}")
    fun updateExamSection(
        @PathVariable("id") id: String, @RequestBody @Valid examSectionDto: ExamSectionDto
    ): ResponseEntity<ExamSection> {
        return toOkResponseEntity(examSectionService.updateExamSection(id, examSectionDto))
    }

    @DeleteMapping("/{id}")
    fun deleteExamSection(@PathVariable("id") id: String): ResponseEntity<Void> {
        examSectionService.deleteExamSection(id)
        return ResponseEntity.noContent().build()
    }
}