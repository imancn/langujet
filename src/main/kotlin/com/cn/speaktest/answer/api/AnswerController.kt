package com.cn.speaktest.answer.api

import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.toOkMessage
import com.cn.speaktest.answer.api.request.ChoiceAnswerRequest
import com.cn.speaktest.answer.api.request.TextAnswerRequest
import com.cn.speaktest.answer.api.request.TrueFalseAnswerRequest
import com.cn.speaktest.answer.api.request.VoiceAnswerRequest
import com.cn.speaktest.answer.AnswerService
import org.springframework.web.bind.annotation.*

private const val SUBMIT_MESSAGE = "Answer Submitted"

@RestController
@RequestMapping("/answers")
class AnswerController(
    private val answerService: AnswerService,
) {

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