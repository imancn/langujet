package com.cn.speaktest.actor.exam.api

import com.cn.speaktest.application.advice.Message
import com.cn.speaktest.application.advice.toOkMessage
import com.cn.speaktest.domain.exam.service.ExamIssueService
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/exam-issues")
@Validated
class ExamIssueController(private val examIssueService: ExamIssueService) {

    @GetMapping("/{id}")
    fun findById(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank id: String?
    ): Message {
        examIssueService.preAuthCheck(auth!!, id!!)
        return examIssueService.findById(id).toOkMessage()
    }

    @GetMapping("/exam-session/{id}")
    fun findByExamSessionId(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank id: String?
    ): Message {
        return examIssueService.findByExamSessionId(auth!!, id!!).toOkMessage()
    }

    @GetMapping("/answer/{id}")
    fun findByAnswerId(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank id: String?
    ): Message {
        return examIssueService.findByAnswerId(auth!!, id!!).toOkMessage()
    }
}