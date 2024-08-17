package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.*
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.domain.exam.service.ExamSessionService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
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
    @PostMapping("/student/exam-sessions/enroll")
    fun enrollExamSession(
        @RequestParam @NotBlank userId: String,
        @RequestParam @NotBlank examServiceId: String,
        @RequestParam examId: String?,
    ): ExamSessionEnrollResponse = examSessionService.enrollExamSession(userId, examServiceId, examId)
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/exam-sessions/details")
    fun getStudentExamSession(
        @RequestParam @NotBlank examSessionId: String
    ): ExamSessionDetailsResponse = examSessionService.getStudentExamSessionDetailsResponse(examSessionId)

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-sessions/search")
    fun searchStudentExamSessions(
        @RequestBody @Valid request: ExamSessionSearchStudentRequest
    ): CustomPage<ExamSessionSearchResponse> {
        return examSessionService.searchExamSessions(request)
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/exam-sessions/search")
    fun searchStudentExamSessionsByAdmin(
        @RequestBody @Valid request: ExamSessionSearchAdminRequest
    ): CustomPage<ExamSessionSearchResponse> {
        return examSessionService.searchExamSessions(request)
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-sessions/section")
    fun getExamSection(
        @RequestParam @NotBlank examSessionId: String,
        @RequestParam sectionOrder: Int
    ): SectionDTO = examSessionService.getExamSection(examSessionId, sectionOrder)

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-sessions/finish")
    fun finishExamSession(
        @RequestParam @NotBlank examSessionId: String?
    ): ExamSessionFinishResponse = examSessionService.finishExamSession(examSessionId!!)
}