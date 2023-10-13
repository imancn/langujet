package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.ExamRequest
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType
import com.cn.langujet.domain.exam.service.ExamRequestService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/exam-requests")
class ExamRequestController(
    private val examRequestService: ExamRequestService
) {
    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT')")
    fun createExamRequest(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestParam @NotBlank examType: ExamType?,
        @RequestParam sectionType: SectionType?
    ): ResponseEntity<ExamRequest> {

        return toOkResponseEntity(
            examRequestService.createExamRequest(auth!!, examType!!, sectionType)
        )
    }
    @GetMapping("/by-student-id")
    @PreAuthorize("hasAnyRole('STUDENT') ")
    fun getExamRequests(
        @RequestHeader("Authorization") @NotBlank auth: String?,
    ): ResponseEntity<List<ExamRequest>> =
        toOkResponseEntity(examRequestService.getExamRequests(auth!!))
}