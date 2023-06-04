package com.cn.speaktest.actor.exam.api

import com.cn.speaktest.actor.exam.payload.request.ExamRequest
import com.cn.speaktest.application.advice.Message
import com.cn.speaktest.application.advice.toOkMessage
import com.cn.speaktest.domain.exam.model.Currency
import com.cn.speaktest.domain.exam.model.Difficulty
import com.cn.speaktest.domain.exam.model.ExamInfo
import com.cn.speaktest.domain.exam.model.Price
import com.cn.speaktest.domain.exam.service.ExamService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
        @RequestParam @NotBlank description: String?,
        @RequestParam @NotNull sectionsNumber: Int?,
        @RequestParam @NotNull questionNumber: Int?,
        @RequestParam @NotNull examDuration: Long?,
        @RequestParam @NotNull difficulty: Difficulty?,
        @RequestParam @NotNull priceValue: Double?,
        @RequestParam @NotNull priceCurrency: Currency?,
    ): Message {
        return examService.createExam(
            ExamInfo(
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
    fun updateExam(
        @PathVariable @NotBlank id: String?,
        @RequestParam name: String?,
        @RequestParam description: String?,
        @RequestParam sectionsNumber: Int?,
        @RequestParam questionNumber: Int?,
        @RequestParam examDuration: Long?,
        @RequestParam difficulty: Difficulty,
        @RequestParam priceValue: Double?,
        @RequestParam priceCurrency: Currency?,
    ): Message {
        return examService.updateExam(
            id!!,
            ExamRequest(
                name,
                description,
                sectionsNumber,
                questionNumber,
                examDuration,
                difficulty,
                priceValue,
                priceCurrency
            )
        ).toOkMessage()
    }

    @GetMapping("/{id}")
    fun getExamById(@PathVariable id: String): Message {
        return examService.getExamById(id).toOkMessage()
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllExams(): Message {
        return examService.getAllExams().toOkMessage()
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
        return examService.getAllExamsByFilters(
            id,
            name,
            sectionsNumber,
            questionNumber,
            examDuration,
            PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))
        ).toOkMessage()
    }
}