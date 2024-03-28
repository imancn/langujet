package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamSessionEnrollResponse
import com.cn.langujet.actor.exam.payload.ExamSessionResponse
import com.cn.langujet.actor.exam.payload.ExamSessionSearchRequest
import com.cn.langujet.actor.exam.payload.SectionDTO
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.domain.exam.service.ExamSessionService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
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
        @RequestParam @NotBlank studentId: String?,
        @RequestParam @NotNull examVariantId: String?,
    ): ResponseEntity<ExamSessionEnrollResponse> =
        ResponseEntity.ok(examSessionService.enrollExamSession(studentId!!, examVariantId!!))

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/exam-session/{examSessionId}")
    fun getStudentExamSession(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank examSessionId: String?
    ): ResponseEntity<ExamSessionResponse> =
        ResponseEntity.ok(examSessionService.getStudentExamSessionResponse(auth!!, examSessionId!!))

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-session")
    fun searchStudentExamSessions(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestBody @Valid request: ExamSessionSearchRequest
    ): ResponseEntity<CustomPage<ExamSessionResponse>> =
        ResponseEntity.ok(
            examSessionService.searchExamSessions(auth!!, request)
        )
    
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-session/section")
    fun getExamSection(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotNull sectionOrder: Int?
    ): ResponseEntity<SectionDTO> =
        ResponseEntity.ok(examSessionService.getExamSection(auth!!, examSessionId!!, sectionOrder!!))

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/exam-session/finish")
    fun finishExamSession(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestParam @NotBlank examSessionId: String?
    ): ResponseEntity<ExamSessionResponse> =
        ResponseEntity.ok(examSessionService.finishExamSession(auth!!, examSessionId!!))
}