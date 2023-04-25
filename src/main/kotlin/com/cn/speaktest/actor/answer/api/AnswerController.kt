package com.cn.speaktest.actor.answer.api

import com.cn.speaktest.actor.answer.payload.request.ChoiceAnswerRequest
import com.cn.speaktest.actor.answer.payload.request.TextAnswerRequest
import com.cn.speaktest.actor.answer.payload.request.TrueFalseAnswerRequest
import com.cn.speaktest.actor.answer.payload.request.VoiceAnswerRequest
import com.cn.speaktest.application.advice.Message
import com.cn.speaktest.application.advice.toOkMessage
import com.cn.speaktest.domain.answer.AnswerService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/answers")
class AnswerController(
    private val answerService: AnswerService,
) {

    private val SUBMIT_MESSAGE = "Answer Submitted"

    @GetMapping("/{examIssueId}")
    fun getAnswersByExamIssueId(@PathVariable examIssueId: String): Message {
        return answerService.getAnswersByExamIssueId(examIssueId).toOkMessage()
    }

    @GetMapping("/{id}")
    fun getAnswerById(@PathVariable id: String): Message {
        return answerService.getAnswerById(id).toOkMessage()
    }

    @PostMapping("/text")
    fun submitTextAnswer(@RequestBody answerRequest: TextAnswerRequest): Message {
        return answerService.submitTextAnswer(answerRequest).toOkMessage(SUBMIT_MESSAGE)
    }

    @PostMapping("/choice")
    fun submitChoiceAnswer(@RequestBody answerRequest: ChoiceAnswerRequest): Message {
        return answerService.submitChoiceAnswer(answerRequest).toOkMessage(SUBMIT_MESSAGE)
    }

    @PostMapping("/true-false")
    fun submitTrueFalseAnswer(@RequestBody answerRequest: TrueFalseAnswerRequest): Message {
        return answerService.submitTrueFalseAnswer(answerRequest).toOkMessage(SUBMIT_MESSAGE)
    }

    @PostMapping("/voice")
    fun submitVoiceAnswer(@RequestBody answerRequest: VoiceAnswerRequest): Message {
        return answerService.submitVoiceAnswer(answerRequest).toOkMessage(SUBMIT_MESSAGE)
    }
}