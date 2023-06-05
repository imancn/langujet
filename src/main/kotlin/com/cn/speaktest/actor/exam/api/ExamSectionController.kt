package com.cn.speaktest.actor.exam.api

import com.cn.speaktest.actor.exam.payload.dto.ExamSectionDto
import com.cn.speaktest.domain.exam.model.ExamSection
import com.cn.speaktest.domain.exam.service.ExamSectionService
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
        val examSection = examSectionService.getExamSectionById(id)
        return ResponseEntity.ok(examSection)
    }

    @PostMapping("")
    fun createExamSection(@RequestBody @Valid examSectionDto: ExamSectionDto): ExamSectionDto {
        return examSectionService.createExamSection(examSectionDto)
    }

    @PutMapping("/{id}")
    fun updateExamSection(
        @PathVariable("id") id: String, @RequestBody @Valid examSectionDto: ExamSectionDto
    ): ResponseEntity<ExamSection> {
        val updatedExamSection = examSectionService.updateExamSection(id, examSectionDto)
        return if (updatedExamSection != null) {
            ResponseEntity.ok(updatedExamSection)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteExamSection(@PathVariable("id") id: String): ResponseEntity<Void> {
        examSectionService.deleteExamSection(id)
        return ResponseEntity.noContent().build()
    }
}