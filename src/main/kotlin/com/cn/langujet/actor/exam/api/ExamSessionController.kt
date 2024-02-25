package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamSessionEnrollResponse
import com.cn.langujet.actor.exam.payload.ExamSessionResponse
import com.cn.langujet.actor.exam.payload.SectionDTO
import com.cn.langujet.domain.exam.model.*
import com.cn.langujet.domain.exam.service.ExamSessionService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.PageRequest
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
        @NotBlank studentId: String?,
        @RequestParam @NotNull examTypeId: String?,
    ): ResponseEntity<ExamSessionEnrollResponse> =
        ResponseEntity.ok(examSessionService.enrollExamSession(studentId!!, examTypeId!!))

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/exam-session/{examSessionId}")
    fun getStudentExamSession(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank examSessionId: String?
    ): ResponseEntity<ExamSessionResponse> =
        ResponseEntity.ok(examSessionService.getStudentExamSessionResponse(auth!!, examSessionId!!))

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/exam-session/all")
    fun getStudentExamSessions(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "0") pageNumber: Int,
    ): ResponseEntity<List<ExamSessionResponse>> =
        ResponseEntity.ok(
            examSessionService.getAllStudentExamSessionResponses(
                auth!!,
                PageRequest.of(pageNumber, pageSize)
            )
        )

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/exam-session/by-state")
    fun getAllStudentExamSessionsByStatus(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestParam @NotNull state: ExamSessionState?,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "0") pageNumber: Int,
    ): ResponseEntity<List<ExamSessionResponse>> =
        ResponseEntity.ok(
            examSessionService.getAllStudentExamSessionResponsesByState(
                auth!!,
                state!!,
                PageRequest.of(pageNumber, pageSize))
        )

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor/exam-session/{examSessionId}")
    fun getProfessorExamSession(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank examSessionId: String?
    ): ResponseEntity<ExamSession> =
        ResponseEntity.ok(examSessionService.getProfessorExamSession(auth!!, examSessionId!!))

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor/exam-session/all")
    fun getAllProfessorExamSessions(
        @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<List<ExamSession>> =
        ResponseEntity.ok(examSessionService.getAllProfessorExamSessions(auth!!))

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