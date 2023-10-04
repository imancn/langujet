package com.cn.langujet.actor.answer.api

import com.cn.langujet.actor.answer.payload.request.TextAnswerRequest
import com.cn.langujet.actor.answer.payload.request.TextIssuesAnswerRequest
import com.cn.langujet.actor.answer.payload.request.TrueFalseAnswerRequest
import com.cn.langujet.actor.answer.payload.request.VoiceAnswerRequest
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.answer.AnswerService
import com.cn.langujet.domain.answer.model.Answer
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/answers")
class AnswerController(
    private val answerService: AnswerService,
) {
    private val SUBMIT_MESSAGE = "Answer Submitted"

    @GetMapping("/{examIssueId}")
    fun getAnswersByExamIssueId(@PathVariable examIssueId: String): List<Answer> =
        answerService.getAnswersByExamIssueId(examIssueId)

    @GetMapping("/{id}")
    fun getAnswerById(
        @PathVariable id: String, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Answer> = toOkResponseEntity(answerService.getAnswerById(id))

    @PostMapping("/text")
    fun submitTextAnswer(
        @RequestBody request: TextAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        answerService.submitAnswer(request, auth!!)
        return toOkResponseEntity(true)
    }

    @PostMapping("/text-issues")
    fun submitTextIssuesAnswer(
        @RequestBody request: TextIssuesAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        answerService.submitAnswer(request, auth!!)
        return toOkResponseEntity(true)
    }

    @PostMapping("/true-false")
    fun submitTrueFalseAnswer(
        @RequestBody request: TrueFalseAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        answerService.submitAnswer(request, auth!!)
        return toOkResponseEntity(true)
    }

    @PostMapping("/voice")
    fun submitVoiceAnswer(
        @RequestBody request: VoiceAnswerRequest, @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<Boolean> {
        answerService.submitAnswer(request, auth!!)
        return toOkResponseEntity(true)
    }
}