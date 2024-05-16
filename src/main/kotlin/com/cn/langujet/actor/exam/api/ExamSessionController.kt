package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.*
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.domain.exam.service.ExamSessionService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Validated
class ExamSessionController(
    private val examSessionService: ExamSessionService
){

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/student/exam-session/enroll")
    fun enrollExamSession(
        @RequestParam @NotBlank userId: String?,
        @RequestParam @NotNull examVariantId: String?,
    ): ExamSessionEnrollResponse = examSessionService.enrollExamSession(userId!!, examVariantId!!)
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/exam-session/details")
    fun getStudentExamSession(
        @RequestParam @NotBlank examSessionId: String?
    ): ExamSessionDetailsResponse = examSessionService.getStudentExamSessionDetailsResponse(examSessionId!!)

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-session")
    fun searchStudentExamSessions(
        @RequestBody @Valid request: ExamSessionSearchRequest
    ): CustomPage<ExamSessionSearchResponse> = examSessionService.searchExamSessions(request)
    
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-session/section")
    fun getExamSection(
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotNull sectionOrder: Int?
    ): SectionDTO = examSessionService.getExamSection(examSessionId!!, sectionOrder!!)

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-session/finish")
    fun finishExamSession(
        @RequestParam @NotBlank examSessionId: String?
    ): ExamSessionFinishResponse = examSessionService.finishExamSession(examSessionId!!)
}