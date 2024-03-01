package com.cn.langujet.actor.correction.api

import com.cn.langujet.actor.correction.model.CorrectAnswerListDTO
import com.cn.langujet.domain.correction.CorrectAnswerService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
@Validated
class CorrectAnswerController(
    private val correctAnswerService: CorrectAnswerService
) {
    @GetMapping("admin/correct-answers")
    @PreAuthorize("hasRole('ADMIN')")
    fun getCorrectAnswers(
        @RequestParam @NotBlank examId: String?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestParam partId: Int?,
        @RequestParam questionId: Int?
    ): CorrectAnswerListDTO {
        return correctAnswerService.getCorrectAnswer(examId!!, sectionOrder!!, partId, questionId)
    }

    @PostMapping("admin/correct-answers")
    @PreAuthorize("hasRole('ADMIN')")
    fun createCorrectAnswer(@RequestBody request: CorrectAnswerListDTO?): CorrectAnswerListDTO {
        return correctAnswerService.createCorrectAnswer(request!!)
    }

    @PutMapping("admin/correct-answers")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateCorrectAnswer(
        @RequestBody @NotNull request: CorrectAnswerListDTO?
    ): CorrectAnswerListDTO {
        return correctAnswerService.updateCorrectAnswer(request!!)
    }

    @DeleteMapping("admin/correct-answers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteCorrectAnswer(
        @PathVariable @NotBlank id: String?,
    ) {
        correctAnswerService.deleteCorrectAnswer(id!!)
    }
}