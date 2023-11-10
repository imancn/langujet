package com.cn.langujet.actor.answer.api

import com.cn.langujet.actor.answer.payload.request.TextAnswerRequest
import com.cn.langujet.actor.answer.payload.request.TextIssuesAnswerRequest
import com.cn.langujet.actor.answer.payload.request.TrueFalseAnswerRequest
import com.cn.langujet.actor.answer.payload.request.VoiceAnswerRequest
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.answer.AnswerService
import com.cn.langujet.domain.answer.model.Answer
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/answers")
class AnswerController(
    private val answerService: AnswerService,
) {
    private val SUBMIT_MESSAGE = "Answer Submitted"

    // TODO: Add get by exam session id and section id

    @GetMapping("/{id}")
    fun getAnswerById(
        @PathVariable id: String, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Answer> = toOkResponseEntity(answerService.getAnswerById(id))

    @PostMapping("/text")
    fun submitTextAnswer(
        @RequestBody @Valid request: TextAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        answerService.submitAnswer(request, auth!!)
        return toOkResponseEntity(true)
    }

    @PostMapping("/text-issues")
    fun submitTextIssuesAnswer(
        @RequestBody @Valid request: TextIssuesAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        answerService.submitAnswer(request, auth!!)
        return toOkResponseEntity(true)
    }

    @PostMapping("/true-false")
    fun submitTrueFalseAnswer(
        @RequestBody @Valid request: TrueFalseAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        answerService.submitAnswer(request, auth!!)
        return toOkResponseEntity(true)
    }

    @PostMapping("/voice")
    fun submitVoiceAnswer(
        @RequestBody @Valid request: VoiceAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        answerService.submitAnswer(request, auth!!)
        return toOkResponseEntity(true)
    }
}