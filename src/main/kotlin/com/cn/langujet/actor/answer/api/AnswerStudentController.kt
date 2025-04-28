package com.cn.langujet.actor.answer.api

import com.cn.langujet.actor.answer.payload.request.AnswerBulkRequest
import com.cn.langujet.application.arch.BundleService
import com.cn.langujet.application.arch.controller.payload.response.MessageResponse
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
@RequestMapping("/api/v1/student/answers")
@Validated
class AnswerStudentController(
    private val answerService: AnswerService,
    private val bundleService: BundleService,
) {
    @PostMapping
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun submitBulkAnswers(
        @RequestParam @NotBlank examSessionId: Long?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestBody @Valid request: List<AnswerBulkRequest>,
    ): ResponseEntity<MessageResponse> {
        answerService.submitBulkAnswers(examSessionId, sectionOrder, request)
        return ResponseEntity.ok(bundleService.getMessageResponse("successful"))
    }
    
    @PostMapping("/voices")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun submitVoiceAnswer(
        @RequestParam @NotBlank examSessionId: Long?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestParam @NotNull partOrder: Int?,
        @RequestParam @NotNull questionOrder: Int?,
        @RequestParam("voice") voice: MultipartFile
    ): ResponseEntity<MessageResponse> {
        answerService.submitVoiceAnswer(
            examSessionId!!,
            sectionOrder!!,
            partOrder!!,
            questionOrder!!,
            voice,
        )
        return ResponseEntity.ok(bundleService.getMessageResponse("successful"))
    }
}