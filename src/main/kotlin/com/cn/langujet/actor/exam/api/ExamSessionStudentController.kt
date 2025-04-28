package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.*
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.domain.exam.service.ExamSessionService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/student/exam-sessions")
@Validated
class ExamSessionStudentController(
    private val examSessionService: ExamSessionService
){
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/details")
    fun getStudentExamSession(
        @RequestParam @NotBlank examSessionId: Long
    ): ExamSessionDetailsResponse = examSessionService.getStudentExamSessionDetailsResponse(examSessionId)

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/search")
    fun searchStudentExamSessions(
        @RequestBody @Valid request: ExamSessionSearchStudentRequest
    ): PageResponse<ExamSessionSearchResponse> {
        return examSessionService.searchExamSessions(request)
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-sessions/section")
    fun getExamSection(
        @RequestParam @NotBlank examSessionId: Long,
        @RequestParam sectionOrder: Int
    ): SectionComposite = examSessionService.getExamSection(examSessionId, sectionOrder)

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-sessions/finish")
    fun finishExamSession(
        @RequestParam @NotBlank examSessionId: Long?
    ): ExamSessionFinishResponse = examSessionService.finishExamSession(examSessionId!!)
}