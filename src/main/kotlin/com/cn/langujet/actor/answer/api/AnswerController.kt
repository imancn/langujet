package com.cn.langujet.actor.answer.api

import com.cn.langujet.actor.answer.payload.request.*
import com.cn.langujet.actor.answer.payload.response.CorrectorCorrectionAnswersResponse
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.answer.AnswerService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/")
@Validated
class AnswerController(
    private val answerService: AnswerService,
) {
    @PostMapping("/student/answer/voice")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun submitVoiceAnswer(
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestParam @NotNull partOrder: Int?,
        @RequestParam @NotNull questionOrder: Int?,
        @RequestParam("voice") voice: MultipartFile
    ): ResponseEntity<Boolean> {
        answerService.submitVoiceAnswer(
            examSessionId!!,
            sectionOrder!!,
            partOrder!!,
            questionOrder!!,
            voice,
        )
        return toOkResponseEntity(true)
    }

    @PostMapping("/student/answer/bulk")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun submitBulkAnswers(
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestBody @Valid request: List<AnswerBulkRequest>,
    ): ResponseEntity<Boolean> {
        return toOkResponseEntity(answerService.submitBulkAnswers(examSessionId, sectionOrder, request))
    }
    
    @GetMapping("/corrector/answer")
    @PreAuthorize("hasRole('ROLE_CORRECTOR')")
    fun getCorrectorCorrectionAnswers(@RequestParam sectionOrder: Int): CorrectorCorrectionAnswersResponse {
        return answerService.getCorrectorCorrectionAnswers(sectionOrder)
    }
}