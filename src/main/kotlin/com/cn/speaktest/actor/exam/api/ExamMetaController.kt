package com.cn.speaktest.actor.exam.api

import com.cn.speaktest.actor.exam.payload.dto.ExamMetaDto
import com.cn.speaktest.application.advice.Message
import com.cn.speaktest.application.advice.toOkMessage
import com.cn.speaktest.domain.exam.model.Difficulty
import com.cn.speaktest.domain.exam.model.ExamMeta
import com.cn.speaktest.domain.exam.model.Price
import com.cn.speaktest.domain.exam.service.ExamMetaService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/exams")
@Validated
class ExamMetaController(private val examMetaService: ExamMetaService) {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createExamMeta(
        @RequestParam @NotBlank name: String?,
        @RequestParam @NotBlank description: String?,
        @RequestParam @NotNull sectionsNumber: Int?,
        @RequestParam @NotNull questionNumber: Int?,
        @RequestParam @NotNull examDuration: Long?,
        @RequestParam @NotNull difficulty: Difficulty?,
        @RequestParam @NotNull priceValue: Double?,
        @RequestParam @NotNull priceCurrency: Currency?,
    ): Message {
        return examMetaService.createExam(
            ExamMeta(
                null,
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
        ).toOkMessage()
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateExamMeta(
        @PathVariable @NotBlank id: String?,
        @RequestBody examMetaDto: ExamMetaDto
    ): Message {
        return examMetaService.updateExam(
            id!!,
            examMetaDto
        ).toOkMessage()
    }

    @GetMapping("/{id}")
    fun getExamById(@PathVariable id: String): Message {
        return examMetaService.getExamById(id).toOkMessage()
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllExams(): Message {
        return examMetaService.getAllExams().toOkMessage()
    }

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
    ): Message {
        return examMetaService.getAllExamsByFilters(
            id,
            name,
            sectionsNumber,
            questionNumber,
            examDuration,
            PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))
        ).toOkMessage()
    }
}