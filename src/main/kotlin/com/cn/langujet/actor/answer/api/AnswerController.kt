package com.cn.langujet.actor.answer.api

import com.cn.langujet.actor.answer.payload.request.ChoiceAnswerRequest
import com.cn.langujet.actor.answer.payload.request.TextAnswerRequest
import com.cn.langujet.actor.answer.payload.request.TrueFalseAnswerRequest
import com.cn.langujet.actor.answer.payload.request.VoiceAnswerRequest
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.answer.AnswerService
import com.cn.langujet.domain.answer.model.Answer
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
    fun getAnswerById(@PathVariable id: String): ResponseEntity<Answer> =
        toOkResponseEntity(answerService.getAnswerById(id))

    @PostMapping("/text")
    fun submitTextAnswer(@RequestBody answerRequest: TextAnswerRequest): ResponseEntity<Answer.Text> =
        toOkResponseEntity( answerService.submitTextAnswer(answerRequest))

    @PostMapping("/choice")
    fun submitChoiceAnswer(@RequestBody answerRequest: ChoiceAnswerRequest): ResponseEntity<Answer.Choice> =
        toOkResponseEntity(answerService.submitChoiceAnswer(answerRequest))

    @PostMapping("/true-false")
    fun submitTrueFalseAnswer(@RequestBody answerRequest: TrueFalseAnswerRequest): ResponseEntity<Answer.TrueFalse> =
        toOkResponseEntity(answerService.submitTrueFalseAnswer(answerRequest))

    @PostMapping("/voice")
    fun submitVoiceAnswer(@RequestBody answerRequest: VoiceAnswerRequest): ResponseEntity<Answer.Voice> =
        toOkResponseEntity(answerService.submitVoiceAnswer(answerRequest))
}