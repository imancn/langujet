package com.cn.langujet.actor.answer.api

import com.cn.langujet.actor.answer.payload.request.*
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.answer.AnswerService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/")
@Validated
class AnswerController(
    private val answerService: AnswerService,
) {
    // TODO: Add get by exam session id and section id

    @PostMapping("/student/answer/text")
    fun submitTextAnswer(
        @RequestBody @Valid request: TextAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        return toOkResponseEntity(answerService.submitAnswer(request, auth!!))
    }

    @PostMapping("/student/answer/text-issues")
    fun submitTextIssuesAnswer(
        @RequestBody @Valid request: TextIssuesAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        return toOkResponseEntity(answerService.submitAnswer(request, auth!!))
    }

    @PostMapping("/student/answer/true-false")
    fun submitTrueFalseAnswer(
        @RequestBody @Valid request: TrueFalseAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        return toOkResponseEntity(answerService.submitAnswer(request, auth!!))
    }

    @PostMapping("/student/answer/multiple-choice")
    fun submitMultipleChoiceAnswer(
        @RequestBody @Valid request: MultipleChoiceAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        return toOkResponseEntity(answerService.submitAnswer(request, auth!!))
    }

    @PostMapping("/student/answer/voice")
    fun submitVoiceAnswer(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestParam @NotNull partIndex: Int?,
        @RequestParam @NotNull questionIndex: Int?,
        @RequestParam("voice") voice: MultipartFile
    ): ResponseEntity<Boolean> {
        answerService.submitVoiceAnswer(
            auth!!,
            examSessionId!!,
            sectionOrder!!,
            partIndex!!,
            questionIndex!!,
            voice,
        )
        return toOkResponseEntity(true)
    }

    @PostMapping("/student/answer/bulk/text")
    fun submitBulkTextAnswer(
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestBody @Valid request: List<TextBulkAnswerRequest>,
        @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        return toOkResponseEntity(answerService.submitBulkAnswers(examSessionId, sectionOrder, request, auth!!))
    }

    @PostMapping("/student/answer/bulk/text-issues")
    fun submitBulkTextIssuesAnswer(
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestBody @Valid request: List<TextIssuesBulkAnswerRequest>,
        @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        return toOkResponseEntity(answerService.submitBulkAnswers(examSessionId, sectionOrder, request, auth!!))
    }

    @PostMapping("/student/answer/bulk/true-false")
    fun submitBulkTrueFalseAnswer(
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestBody @Valid request: List<TrueFalseBulkAnswerRequest>,
        @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        return toOkResponseEntity(answerService.submitBulkAnswers(examSessionId, sectionOrder, request, auth!!))
    }

    @PostMapping("/student/answer/bulk/multiple-choice")
    fun submitBulkMultipleChoiceAnswer(
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestBody @Valid request: List<MultipleChoiceBulkAnswerRequest>,
        @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        return toOkResponseEntity(answerService.submitBulkAnswers(examSessionId, sectionOrder, request, auth!!))
    }
}