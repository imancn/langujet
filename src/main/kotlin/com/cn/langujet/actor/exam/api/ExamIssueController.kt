package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.ExamIssue
import com.cn.langujet.domain.exam.service.ExamIssueService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<ExamIssue> {
        return toOkResponseEntity(
            examIssueService.findById(id!!).also {
                examIssueService.preAuthCheck(auth!!, id)
            }
        )

    }

    @GetMapping("/exam-session/{id}")
    fun findByExamSessionId(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank id: String?
    ): ResponseEntity<List<ExamIssue>> {
        return toOkResponseEntity(
            examIssueService.findByExamSessionId(auth!!, id!!).filter {
                examIssueService.preAuthCheck(auth, it.id ?: "")
            }
        )
    }

    @GetMapping("/answer/{id}")
    fun findByAnswerId(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank id: String?
    ): ResponseEntity<ExamIssue> {
        return toOkResponseEntity(examIssueService.findByAnswerId(auth!!, id!!).also { answer ->
            examIssueService.preAuthCheck(auth, answer.id!!)
        })
    }
}